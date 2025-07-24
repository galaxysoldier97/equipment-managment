package mc.monacotelecom.tecrep.equipments.process.ancillary.searcher;

import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.assembler.AncillaryEquipmentResourceAssembler;
import mc.monacotelecom.tecrep.equipments.connectors.IfsConnector;
import mc.monacotelecom.tecrep.equipments.connectors.dto.IFSBoxResponse;
import mc.monacotelecom.tecrep.equipments.dto.AncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.EquipmentModelCreateDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchAncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.AncillaryEquipmentDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.entity.Equipment;
import mc.monacotelecom.tecrep.equipments.entity.EquipmentModel;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentCategory;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentName;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentNature;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmNotFoundException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.mapper.AncillaryMapper;
import mc.monacotelecom.tecrep.equipments.process.IEquipmentModelProcess;
import mc.monacotelecom.tecrep.equipments.repository.AncillaryRepository;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentRepository;
import mc.monacotelecom.tecrep.equipments.repository.ProviderRepository;
import mc.monacotelecom.tecrep.equipments.repository.WarehouseRepository;
import mc.monacotelecom.tecrep.equipments.state.StateMachineService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static mc.monacotelecom.tecrep.equipments.enums.AccessType.FTTH;
import static mc.monacotelecom.tecrep.equipments.enums.EquipmentModelCategory.ANCILLARY;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.NO_PROVIDER;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.NO_WAREHOUSE;

@Slf4j
@Component
@Profile("epic")
public class AncillarySearcherWithIfs extends AbstractAncillarySearcher {
    private final IEquipmentModelProcess equipmentModelProcess;
    private final ProviderRepository providerRepository;
    private final WarehouseRepository warehouseRepository;
    private final IfsConnector ifsConnector;

    @SuppressWarnings("java:S107")
    public AncillarySearcherWithIfs(final AncillaryRepository ancillaryRepository,
                                    final EquipmentRepository<Equipment> equipmentRepository,
                                    final LocalizedMessageBuilder localizedMessageBuilder,
                                    final StateMachineService stateMachineService,
                                    final IEquipmentModelProcess equipmentModelProcess,
                                    final ProviderRepository providerRepository,
                                    final WarehouseRepository warehouseRepository,
                                    final IfsConnector ifsConnector,
                                    final AncillaryMapper ancillaryMapper) {
        super(ancillaryRepository,
                AncillaryEquipmentResourceAssembler.of(AncillarySearcherWithIfs.class, equipmentRepository, ancillaryMapper),
                localizedMessageBuilder,
                stateMachineService,
                ancillaryMapper);
        this.equipmentModelProcess = equipmentModelProcess;
        this.providerRepository = providerRepository;
        this.warehouseRepository = warehouseRepository;
        this.ifsConnector = ifsConnector;
    }

    @Override
    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<AncillaryEquipmentDTO> searchV1(SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO, Pageable pageable, PagedResourcesAssembler<AncillaryEquipment> assembler) {
        return mapToDTOsV1(assembler, commonSearch(searchAncillaryEquipmentDTO, pageable));
    }

    @Override
    public Page<AncillaryEquipmentDTOV2> search(SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO, Pageable pageable) {
        return mapToDTOs(commonSearch(searchAncillaryEquipmentDTO, pageable));
    }

    private Page<AncillaryEquipment> commonSearch(SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO, Pageable pageable) {
        Specification<AncillaryEquipment> specification = AbstractAncillarySearcher.prepareSpecification(searchAncillaryEquipmentDTO);
        Page<AncillaryEquipment> ancillaryEquipments = ancillaryRepository.findAll(specification, pageable);

        if (ancillaryEquipments.isEmpty() && StringUtils.isNotBlank(searchAncillaryEquipmentDTO.getSerialNumber())) {
            log.info("Start searching on IFS");
            IFSBoxResponse response;
            try {
                response = ifsConnector.checkBox(searchAncillaryEquipmentDTO.getSerialNumber());
            } catch (Exception e) {
                log.error(e.getMessage());
                return ancillaryEquipments;
            }
            var ancillaryEquipment = new AncillaryEquipment();
            ancillaryEquipment.setSerialNumber(response.getSerialNo());
            String modelName = response.getDescription();
            if (modelName != null && modelName.length() > 60) {
                modelName = modelName.substring(0, 59);
            }
            EquipmentModel equipmentModel;
            try {
                equipmentModel = this.equipmentModelProcess.getByNameAndCategory(modelName, ANCILLARY);
            } catch (EqmNotFoundException e) {
                // Creating model on-the-fly
                var equipmentModelDto = new EquipmentModelCreateDTO();
                equipmentModelDto.setName(modelName);
                equipmentModelDto.setCategory(ANCILLARY);
                final var provider = providerRepository.findAll().stream().findFirst().orElseThrow(
                        () -> new EqmValidationException(localizedMessageBuilder, NO_PROVIDER));
                equipmentModelDto.setProviderId(provider.getProviderId());
                equipmentModelDto.setAccessType(FTTH);
                equipmentModel = equipmentModelProcess.create(equipmentModelDto);
            }
            ancillaryEquipment.setModel(equipmentModel);
            ancillaryEquipment.setIndependent(true);
            ancillaryEquipment.setEquipmentName(EquipmentName.BRDBOX);
            ancillaryEquipment.setRecyclable(true);
            ancillaryEquipment.setAccessType(FTTH);
            final var warehouse = warehouseRepository.findAll().stream().findFirst().orElseThrow(
                    () -> new EqmValidationException(localizedMessageBuilder, NO_WAREHOUSE));
            ancillaryEquipment.setWarehouse(warehouse);
            ancillaryEquipment.setStatus(Status.AVAILABLE);
            ancillaryEquipment.setNature(EquipmentNature.MAIN);
            ancillaryEquipment.setCategory(EquipmentCategory.ANCILLARY);
            ancillaryEquipment = ancillaryRepository.save(ancillaryEquipment);
            List<AncillaryEquipment> ancillaryEquipmentList = new ArrayList<>();
            ancillaryEquipmentList.add(ancillaryEquipment);
            ancillaryEquipments = new PageImpl<>(ancillaryEquipmentList, pageable, ancillaryEquipmentList.size());
        } else {
            log.debug("ancillaryEquipments isEmpty {}", ancillaryEquipments.isEmpty());
            log.debug("serialNumber isNotBlank {}", StringUtils.isNotBlank(searchAncillaryEquipmentDTO.getSerialNumber()));
            log.debug("IFS connector is configured {}", Objects.nonNull(ifsConnector));
        }
        return ancillaryEquipments;
    }
}

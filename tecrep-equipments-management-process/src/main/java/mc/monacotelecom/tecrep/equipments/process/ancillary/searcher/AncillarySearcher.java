package mc.monacotelecom.tecrep.equipments.process.ancillary.searcher;

import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.assembler.AncillaryEquipmentResourceAssembler;
import mc.monacotelecom.tecrep.equipments.dto.AncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchAncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.AncillaryEquipmentDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.entity.Equipment;
import mc.monacotelecom.tecrep.equipments.mapper.AncillaryMapper;
import mc.monacotelecom.tecrep.equipments.repository.AncillaryRepository;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentRepository;
import mc.monacotelecom.tecrep.equipments.state.StateMachineService;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

@Profile("!epic")
@Slf4j
@Component
public class AncillarySearcher extends AbstractAncillarySearcher {
    public AncillarySearcher(final AncillaryRepository ancillaryRepository,
                             final EquipmentRepository<Equipment> equipmentRepository,
                             final LocalizedMessageBuilder localizedMessageBuilder,
                             final StateMachineService stateMachineService,
                             final AncillaryMapper ancillaryMapper) {
        super(ancillaryRepository,
                AncillaryEquipmentResourceAssembler.of(AncillarySearcher.class, equipmentRepository, ancillaryMapper),
                localizedMessageBuilder,
                stateMachineService,
                ancillaryMapper);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Override
    public PagedModel<AncillaryEquipmentDTO> searchV1(SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO, Pageable pageable, PagedResourcesAssembler<AncillaryEquipment> assembler) {
        Specification<AncillaryEquipment> specification = AbstractAncillarySearcher.prepareSpecification(searchAncillaryEquipmentDTO);
        return mapToDTOsV1(assembler, ancillaryRepository.findAll(specification, pageable));
    }

    @Override
    public Page<AncillaryEquipmentDTOV2> search(SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO, Pageable pageable) {
        Specification<AncillaryEquipment> specification = AbstractAncillarySearcher.prepareSpecification(searchAncillaryEquipmentDTO);
        return mapToDTOs(ancillaryRepository.findAll(specification, pageable));
    }
}

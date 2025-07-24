package mc.monacotelecom.tecrep.equipments.process.ancillary.searcher;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.inventory.common.CommonFunctions;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.inventory.common.sm.StatusChanger;
import mc.monacotelecom.tecrep.equipments.assembler.AncillaryEquipmentResourceAssembler;
import mc.monacotelecom.tecrep.equipments.dto.AncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchAncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.AncillaryEquipmentDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.mapper.AncillaryMapper;
import mc.monacotelecom.tecrep.equipments.repository.AncillaryRepository;
import mc.monacotelecom.tecrep.equipments.repository.specification.AncillaryEquipmentSpecifications;
import mc.monacotelecom.tecrep.equipments.state.StateMachineService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.ANCILLARY_EQUIPMENT_NOT_FOUND;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequiredArgsConstructor
public abstract class AbstractAncillarySearcher implements IAncillarySearcher {
    protected final AncillaryRepository ancillaryRepository;
    protected final AncillaryEquipmentResourceAssembler ancillaryEquipmentResourceAssembler;
    protected final LocalizedMessageBuilder localizedMessageBuilder;
    protected final StateMachineService stateMachineService;
    protected final AncillaryMapper ancillaryMapper;

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Override
    public PagedModel<AncillaryEquipmentDTO> getAllV1(Pageable pageable, PagedResourcesAssembler<AncillaryEquipment> assembler) {
        Page<AncillaryEquipment> ancillaryEquipments = ancillaryRepository.findAll(pageable);
        PagedModel<AncillaryEquipmentDTO> pr = assembler.toModel(ancillaryEquipments, ancillaryEquipmentResourceAssembler, linkTo(AbstractAncillarySearcher.class).slash("/ancillaryequipments").withSelfRel());
        pr.iterator().forEachRemaining(ancillaryEquipmentDTO -> {
            // Initialize recyclable context
            var ancillaryEquipment = ancillaryRepository.findById(ancillaryEquipmentDTO.getEquipmentId())
                    .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, ANCILLARY_EQUIPMENT_NOT_FOUND, ancillaryEquipmentDTO.getEquipmentId()));
            final StatusChanger<Status, Event> statusEventStatusChanger = stateMachineService.getMachine(ancillaryEquipment.getRecyclable());
            List<Event> serviceTransitions = new ArrayList<>(statusEventStatusChanger.getAvailableEventsWithId(String.valueOf(ancillaryEquipmentDTO.getEquipmentId()), ancillaryEquipmentDTO.getStatus()));
            ancillaryEquipmentDTO.setEvents(serviceTransitions);
        });
        return pr;
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    protected PagedModel<AncillaryEquipmentDTO> mapToDTOsV1(final PagedResourcesAssembler<AncillaryEquipment> assembler, final Page<AncillaryEquipment> ancillaryEquipments) {
        PagedModel<AncillaryEquipmentDTO> pr = assembler.toModel(ancillaryEquipments, ancillaryEquipmentResourceAssembler, linkTo(AbstractAncillarySearcher.class).slash("/ancillaryequipments").withSelfRel());
        pr.iterator().forEachRemaining(ancillaryEquipmentDTO -> {
            // Initialize recyclable context
            final StatusChanger<Status, Event> statusEventStatusChanger = stateMachineService.getMachine(ancillaryEquipmentDTO.getRecyclable());
            List<Event> serviceTransitions = new ArrayList<>(statusEventStatusChanger.getAvailableEventsWithId(String.valueOf(ancillaryEquipmentDTO.getEquipmentId()), ancillaryEquipmentDTO.getStatus()));
            ancillaryEquipmentDTO.setEvents(serviceTransitions);
        });
        return pr;
    }

    protected Page<AncillaryEquipmentDTOV2> mapToDTOs(final Page<AncillaryEquipment> ancillaryEquipments) {
        return ancillaryEquipments.map(ancillary -> {
            var ancillaryDto = ancillaryMapper.toDtoV2(ancillary);
            final StatusChanger<Status, Event> statusEventStatusChanger = stateMachineService.getMachine(ancillary.getRecyclable());
            List<Event> serviceTransitions = new ArrayList<>(statusEventStatusChanger.getAvailableEventsWithId(String.valueOf(ancillary.getEquipmentId()), ancillary.getStatus()));
            ancillaryDto.setEvents(serviceTransitions);
            return ancillaryDto;
        });
    }

    public static Specification<AncillaryEquipment> prepareSpecification(final SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO) {
        Specification<AncillaryEquipment> specification;

        specification = StringUtils.isNotBlank(searchAncillaryEquipmentDTO.getSerialNumber()) ? Specification.where(AncillaryEquipmentSpecifications.containSerialNumber(searchAncillaryEquipmentDTO.getSerialNumber())) : null;
        specification = Objects.nonNull(searchAncillaryEquipmentDTO.getIndependent()) ? CommonFunctions.addSpecificationWithCheck(specification, AncillaryEquipmentSpecifications.isIndependent(), AncillaryEquipmentSpecifications.isNotIndependent(), searchAncillaryEquipmentDTO.getIndependent()) : specification;
        specification = StringUtils.isNotBlank(searchAncillaryEquipmentDTO.getMacAddress()) ? CommonFunctions.addSpecification(specification, AncillaryEquipmentSpecifications.hasMacAddress(searchAncillaryEquipmentDTO.getMacAddress())) : specification;
        specification = StringUtils.isNotBlank(searchAncillaryEquipmentDTO.getModelName()) ? CommonFunctions.addSpecification(specification, AncillaryEquipmentSpecifications.hasModelName(searchAncillaryEquipmentDTO.getModelName())) : specification;
        specification = Objects.nonNull(searchAncillaryEquipmentDTO.getEquipmentName()) ? CommonFunctions.addSpecification(specification, AncillaryEquipmentSpecifications.hasEquipmentName(searchAncillaryEquipmentDTO.getEquipmentName())) : specification;
        specification = Objects.nonNull(searchAncillaryEquipmentDTO.getEquipment()) ? CommonFunctions.addSpecificationWithCheck(specification, AncillaryEquipmentSpecifications.hasEquipment(), AncillaryEquipmentSpecifications.hasNoEquipment(), searchAncillaryEquipmentDTO.getEquipment()) : specification;
        specification = Objects.nonNull(searchAncillaryEquipmentDTO.getStatus()) ? CommonFunctions.addSpecification(specification, AncillaryEquipmentSpecifications.hasStatus(searchAncillaryEquipmentDTO.getStatus())) : specification;
        specification = Objects.nonNull(searchAncillaryEquipmentDTO.getNature()) ? CommonFunctions.addSpecification(specification, AncillaryEquipmentSpecifications.hasNature(searchAncillaryEquipmentDTO.getNature())) : specification;
        specification = StringUtils.isNotBlank(searchAncillaryEquipmentDTO.getProvider()) ? CommonFunctions.addSpecification(specification, AncillaryEquipmentSpecifications.hasProviderName(searchAncillaryEquipmentDTO.getProvider())) : specification;
        specification = StringUtils.isNotBlank(searchAncillaryEquipmentDTO.getWarehouse()) ? CommonFunctions.addSpecification(specification, AncillaryEquipmentSpecifications.hasWarehouse(searchAncillaryEquipmentDTO.getWarehouse())) : specification;
        specification = Objects.nonNull(searchAncillaryEquipmentDTO.getPairedEquipmentId()) ? CommonFunctions.addSpecification(specification, AncillaryEquipmentSpecifications.hasPairedEquipmentId(searchAncillaryEquipmentDTO.getPairedEquipmentId())) : specification;
        specification = Objects.nonNull(searchAncillaryEquipmentDTO.getAccessType()) ? CommonFunctions.addSpecification(specification, AncillaryEquipmentSpecifications.hasAccessType(searchAncillaryEquipmentDTO.getAccessType())) : specification;
        specification = StringUtils.isNotBlank(searchAncillaryEquipmentDTO.getOrderId()) ? CommonFunctions.addSpecification(specification, AncillaryEquipmentSpecifications.hasOrderId(searchAncillaryEquipmentDTO.getOrderId())) : specification;
        specification = Objects.nonNull(searchAncillaryEquipmentDTO.getOrder()) ? CommonFunctions.addSpecificationWithCheck(specification, AncillaryEquipmentSpecifications.hasOrder(), AncillaryEquipmentSpecifications.hasNoOrder(), searchAncillaryEquipmentDTO.getOrder()) : specification;
        specification = StringUtils.isNotBlank(searchAncillaryEquipmentDTO.getExternalNumber()) ? CommonFunctions.addSpecification(specification, AncillaryEquipmentSpecifications.hasExternalNumber(searchAncillaryEquipmentDTO.getExternalNumber())) : specification;
        specification = StringUtils.isNotBlank(searchAncillaryEquipmentDTO.getSfpVersion()) ? CommonFunctions.addSpecification(specification, AncillaryEquipmentSpecifications.hasSfpVersion(searchAncillaryEquipmentDTO.getSfpVersion())) : specification;
        specification = Objects.nonNull(searchAncillaryEquipmentDTO.getServiceId()) ? CommonFunctions.addSpecification(specification, AncillaryEquipmentSpecifications.hasServiceId(searchAncillaryEquipmentDTO.getServiceId())) : specification;
        specification = StringUtils.isNotBlank(searchAncillaryEquipmentDTO.getBatchNumber()) ? CommonFunctions.addSpecification(specification, AncillaryEquipmentSpecifications.hasBatchNumber(searchAncillaryEquipmentDTO.getBatchNumber())) : specification;

        return specification;
    }
}

package mc.monacotelecom.tecrep.equipments.process.simcard.searcher;

import mc.monacotelecom.inventory.common.CommonFunctions;
import mc.monacotelecom.inventory.common.sm.StatusChanger;
import mc.monacotelecom.tecrep.equipments.assembler.SimCardResourceAssembler;
import mc.monacotelecom.tecrep.equipments.dto.SimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchSimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.SimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.mapper.SimCardMapper;
import mc.monacotelecom.tecrep.equipments.repository.SimCardRepository;
import mc.monacotelecom.tecrep.equipments.repository.specification.SimCardSpecifications;
import mc.monacotelecom.tecrep.equipments.state.StateMachineService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;
import static mc.monacotelecom.inventory.common.CommonFunctions.addSpecification;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public abstract class AbstractSimCardSearcher implements ISimCardSearcher {
    protected final SimCardResourceAssembler simCardResourceAssembler;
    protected final StateMachineService stateMachineService;
    protected final SimCardRepository simCardRepository;
    protected final SimCardMapper simCardMapper;

    protected AbstractSimCardSearcher(final StateMachineService stateMachineService,
                                      final SimCardRepository simCardRepository,
                                      final SimCardMapper simCardMapper) {
        this.stateMachineService = stateMachineService;
        this.simCardRepository = simCardRepository;
        this.simCardMapper = simCardMapper;
        this.simCardResourceAssembler = SimCardResourceAssembler.of(this.getClass(), simCardMapper);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<SimCardDTO> getAllV1(Pageable pageable, PagedResourcesAssembler<SimCard> assembler) {
        return mapToDTOsV1(assembler, simCardRepository.findAll(pageable));
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    protected PagedModel<SimCardDTO> mapToDTOsV1(final PagedResourcesAssembler<SimCard> assembler,
                                                 final Page<SimCard> simCards) {
        PagedModel<SimCardDTO> pr = assembler.toModel(simCards, simCardResourceAssembler, linkTo(this.getClass()).slash("/simcards").withSelfRel());
        pr.iterator().forEachRemaining(simCardDTO -> {
            StatusChanger<Status, Event> statusEventStatusChanger = stateMachineService.getMachine(simCardDTO.getRecyclable());
            List<Event> serviceTransitions = new ArrayList<>(statusEventStatusChanger.getAvailableEventsWithId(String.valueOf(simCardDTO.getEquipmentId()), simCardDTO.getStatus()));
            simCardDTO.setEvents(serviceTransitions);
        });

        return pr;
    }

    protected Page<SimCardDTOV2> mapToDTOsV2(final Page<SimCard> simCards) {
        return simCards.map(simCard -> {
            var simCardDto = simCardMapper.toDtoV2(simCard);
            StatusChanger<Status, Event> statusEventStatusChanger = stateMachineService.getMachine(simCard.getRecyclable());
            List<Event> serviceTransitions = new ArrayList<>(statusEventStatusChanger.getAvailableEventsWithId(String.valueOf(simCard.getEquipmentId()), simCard.getStatus()));
            simCardDto.setEvents(serviceTransitions);
            return simCardDto;
        });
    }

    @SuppressWarnings("java:S3776")
    public static Specification<SimCard> prepareSpecifications(final SearchSimCardDTO searchSimCardDTO) {
        Specification<SimCard> specification = null;

        specification = isNotBlank(searchSimCardDTO.getImsi()) ? Specification.where(SimCardSpecifications.imsiNumberStartWith(searchSimCardDTO.getImsi())) : specification;
        specification = isNotBlank(searchSimCardDTO.getSn()) ? addSpecification(specification, SimCardSpecifications.serialNumberStartWith(searchSimCardDTO.getSn())) : specification;
        specification = nonNull(searchSimCardDTO.getStatus()) ? addSpecification(specification, SimCardSpecifications.hasStatus(searchSimCardDTO.getStatus())) : specification;
        specification = isNotBlank(searchSimCardDTO.getImsisn()) ? addSpecification(specification, SimCardSpecifications.imsiSponsorNumberNumberStartWith(searchSimCardDTO.getImsisn())) : specification;
        specification = nonNull(searchSimCardDTO.getNature()) ? addSpecification(specification, SimCardSpecifications.hasNature(searchSimCardDTO.getNature())) : specification;
        specification = isNotBlank(searchSimCardDTO.getPsn()) ? addSpecification(specification, SimCardSpecifications.hasPairedEquipmentSerialNumber(searchSimCardDTO.getPsn())) : specification;
        specification = isNotBlank(searchSimCardDTO.getProvider()) ? addSpecification(specification, SimCardSpecifications.hasProviderName(searchSimCardDTO.getProvider())) : specification;
        specification = isNotBlank(searchSimCardDTO.getWarehouse()) ? addSpecification(specification, SimCardSpecifications.hasWarehouse(searchSimCardDTO.getWarehouse())) : specification;
        specification = isNotBlank(searchSimCardDTO.getServiceId()) ? addSpecification(specification, SimCardSpecifications.hasServiceId(searchSimCardDTO.getServiceId())) : specification;
        specification = nonNull(searchSimCardDTO.getService()) ? CommonFunctions.addSpecificationWithCheck(specification, SimCardSpecifications.hasService(), SimCardSpecifications.hasNoService(), searchSimCardDTO.getService()) : specification;
        specification = isNotBlank(searchSimCardDTO.getOrderId()) ? addSpecification(specification, SimCardSpecifications.hasOrderId(searchSimCardDTO.getOrderId())) : specification;
        specification = nonNull(searchSimCardDTO.getOrder()) ? CommonFunctions.addSpecificationWithCheck(specification, SimCardSpecifications.hasOrder(), SimCardSpecifications.hasNoOrder(), searchSimCardDTO.getOrder()) : specification;
        specification = nonNull(searchSimCardDTO.getAccessType()) ? addSpecification(specification, SimCardSpecifications.hasAccessType(searchSimCardDTO.getAccessType())) : specification;
        specification = isNotBlank(searchSimCardDTO.getExternalNumber()) ? addSpecification(specification, SimCardSpecifications.externalNumberStartWith(searchSimCardDTO.getExternalNumber())) : specification;
        specification = nonNull(searchSimCardDTO.getPreactivated()) ? addSpecification(specification, SimCardSpecifications.hasPreactivated(searchSimCardDTO.getPreactivated())) : specification;
        specification = isNotBlank(searchSimCardDTO.getBatchNumber()) ? addSpecification(specification, SimCardSpecifications.hasBatchNumber(searchSimCardDTO.getBatchNumber())) : specification;
        specification = isNotBlank(searchSimCardDTO.getPackId()) ? addSpecification(specification, SimCardSpecifications.hasPackId(searchSimCardDTO.getPackId())) : specification;
        specification = isNotBlank(searchSimCardDTO.getInventoryPoolCode()) ? addSpecification(specification, SimCardSpecifications.hasInventoryPoolCode(searchSimCardDTO.getInventoryPoolCode())) : specification;
        specification = isNotBlank(searchSimCardDTO.getInventoryPoolId()) ? addSpecification(specification, SimCardSpecifications.hasInventoryPoolId(searchSimCardDTO.getInventoryPoolId())) : specification;
        specification = isNotBlank(searchSimCardDTO.getAllotmentId()) ? addSpecification(specification, SimCardSpecifications.hasAllotmentId(searchSimCardDTO.getAllotmentId())) : specification;
        specification = isNotBlank(searchSimCardDTO.getNumber()) ? addSpecification(specification, SimCardSpecifications.hasNumber(searchSimCardDTO.getNumber())) : specification;
        specification = nonNull(searchSimCardDTO.getEsim()) ? addSpecification(specification, SimCardSpecifications.isESim(searchSimCardDTO.getEsim())) : specification;
        specification = isNotBlank(searchSimCardDTO.getPin1Code()) ? addSpecification(specification, SimCardSpecifications.hasPin1Code(searchSimCardDTO.getPin1Code())) : specification;
        specification = isNotBlank(searchSimCardDTO.getPin2Code()) ? addSpecification(specification, SimCardSpecifications.hasPin2Code(searchSimCardDTO.getPin2Code())) : specification;
        specification = isNotBlank(searchSimCardDTO.getPuk1Code()) ? addSpecification(specification, SimCardSpecifications.hasPuk1Code(searchSimCardDTO.getPuk1Code())) : specification;
        specification = isNotBlank(searchSimCardDTO.getPuk2Code()) ? addSpecification(specification, SimCardSpecifications.hasPuk2Code(searchSimCardDTO.getPuk2Code())) : specification;
        specification = isNotBlank(searchSimCardDTO.getPlmnCode()) ? addSpecification(specification, SimCardSpecifications.hasPlmnCode(searchSimCardDTO.getPlmnCode())) : specification;
        specification = isNotBlank(searchSimCardDTO.getBrand()) ? addSpecification(specification, SimCardSpecifications.hasBrand(searchSimCardDTO.getBrand())) : specification;
        specification = isNotBlank(searchSimCardDTO.getSimProfile()) ? addSpecification(specification, SimCardSpecifications.hasSimProfile(searchSimCardDTO.getSimProfile())) : specification;

        return specification;
    }
}

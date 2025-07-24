package mc.monacotelecom.tecrep.equipments.process.cpe;

import mc.monacotelecom.inventory.common.CommonFunctions;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.inventory.common.sm.StatusChanger;
import mc.monacotelecom.tecrep.equipments.assembler.CPEResourceAssembler;
import mc.monacotelecom.tecrep.equipments.dto.CPEDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchCpeDto;
import mc.monacotelecom.tecrep.equipments.dto.v2.CPEDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.CPE;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmNotFoundException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.mapper.CPEMapper;
import mc.monacotelecom.tecrep.equipments.repository.CPERepository;
import mc.monacotelecom.tecrep.equipments.repository.specification.CPESpecifications;
import mc.monacotelecom.tecrep.equipments.state.StateMachineService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static mc.monacotelecom.inventory.common.Lambdas.verifyOrThrow;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.CPE_MAC_ADDRESS_NOT_VALID_PATTERN;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.CPE_NOT_FOUND;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class CPESearcher {
    private final CPERepository cpeRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final StateMachineService stateMachineService;
    private final CPEMapper cpeMapper;
    private final CPEResourceAssembler cpeResourceAssembler;

    public CPESearcher(final CPERepository cpeRepository,
                       final LocalizedMessageBuilder localizedMessageBuilder,
                       final StateMachineService stateMachineService,
                       final CPEMapper cpeMapper) {
        this.cpeRepository = cpeRepository;
        this.localizedMessageBuilder = localizedMessageBuilder;
        this.stateMachineService = stateMachineService;
        this.cpeMapper = cpeMapper;
        this.cpeResourceAssembler = CPEResourceAssembler.of(CPECreator.class, cpeMapper);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<CPEDTO> getAllV1(Pageable pageable, PagedResourcesAssembler<CPE> assembler) {
        Page<CPE> cpes = cpeRepository.findAll(pageable);
        return mapToDTOsV1(assembler, cpes);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<CPEDTO> searchV1(SearchCpeDto searchCpeDto, Pageable pageable, PagedResourcesAssembler<CPE> assembler) {
        Specification<CPE> specification = prepareSpecification(searchCpeDto);
        return mapToDTOsV1(assembler, cpeRepository.findAll(specification, pageable));
    }

    public Page<CPEDTOV2> search(SearchCpeDto searchCpeDto, Pageable pageable) {
        verifyOrThrow.test((StringUtils.isNotBlank(searchCpeDto.getMacAddress())  ),
                new EqmValidationException(localizedMessageBuilder, CPE_MAC_ADDRESS_NOT_VALID_PATTERN, searchCpeDto.getMacAddress()));

        Specification<CPE> specification = prepareSpecification(searchCpeDto);
        return mapToDTOs(cpeRepository.findAll(specification, pageable));
    }

    private Page<CPEDTOV2> mapToDTOs(Page<CPE> cpes) {
        return cpes.map(cpe -> {
            var cpeDto = cpeMapper.toDtoV2(cpe);
            StatusChanger<Status, Event> statusEventStatusChanger = stateMachineService.getMachine(cpe.getRecyclable());
            List<Event> serviceTransitions = new ArrayList<>(statusEventStatusChanger.getAvailableEventsWithId(String.valueOf(cpe.getEquipmentId()), cpe.getStatus()));
            cpeDto.setEvents(serviceTransitions);
            return cpeDto;
        });
    }

    private PagedModel<CPEDTO> mapToDTOsV1(final PagedResourcesAssembler<CPE> assembler, final Page<CPE> cpes) {
        PagedModel<CPEDTO> pr = assembler.toModel(cpes, cpeResourceAssembler, linkTo(CPESearcher.class).slash("/cpes").withSelfRel());
        pr.iterator().forEachRemaining(cpedto -> {
            // Initialize recyclable context
            CPE cpe = cpeRepository.findByEquipmentId(cpedto.getEquipmentId())
                    .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, CPE_NOT_FOUND, cpedto.getEquipmentId()));

            StatusChanger<Status, Event> statusEventStatusChanger = stateMachineService.getMachine(cpe.getRecyclable());
            List<Event> serviceTransitions = new ArrayList<>(statusEventStatusChanger.getAvailableEventsWithId(String.valueOf(cpedto.getEquipmentId()), cpedto.getStatus()));
            cpedto.setEvents(serviceTransitions);
        });

        return pr;
    }

    @SuppressWarnings("java:S3776")
    public static Specification<CPE> prepareSpecification(SearchCpeDto searchCpeDto) {
        Specification<CPE> specification = null;

        specification = StringUtils.isNotBlank(searchCpeDto.getSerialNumber()) ? Specification.where(CPESpecifications.serialNumberContain(searchCpeDto.getSerialNumber())) : specification;
        specification = StringUtils.isNotBlank(searchCpeDto.getMacAddressCpe()) ? CommonFunctions.addSpecification(specification, CPESpecifications.hasMacAddressCpe(searchCpeDto.getMacAddressCpe())) : specification;
        specification = StringUtils.isNotBlank(searchCpeDto.getMacAddressRouter()) ? CommonFunctions.addSpecification(specification, CPESpecifications.hasMacAddressRouter(searchCpeDto.getMacAddressRouter())) : specification;
        specification = StringUtils.isNotBlank(searchCpeDto.getMacAddressVoip()) ? CommonFunctions.addSpecification(specification, CPESpecifications.hasMacAddressVoip(searchCpeDto.getMacAddressVoip())) : specification;
        specification = StringUtils.isNotBlank(searchCpeDto.getModelName()) ? CommonFunctions.addSpecification(specification, CPESpecifications.hasModelName(searchCpeDto.getModelName())) : specification;
        specification = Objects.nonNull(searchCpeDto.getStatus()) ? CommonFunctions.addSpecification(specification, CPESpecifications.hasStatus(searchCpeDto.getStatus())) : specification;
        specification = Objects.nonNull(searchCpeDto.getNature()) ? CommonFunctions.addSpecification(specification, CPESpecifications.hasNature(searchCpeDto.getNature())) : specification;
        specification = StringUtils.isNotBlank(searchCpeDto.getProvider()) ? CommonFunctions.addSpecification(specification, CPESpecifications.hasProviderName(searchCpeDto.getProvider())) : specification;
        specification = StringUtils.isNotBlank(searchCpeDto.getWarehouse()) ? CommonFunctions.addSpecification(specification, CPESpecifications.hasWarehouse(searchCpeDto.getWarehouse())) : specification;
        specification = Objects.nonNull(searchCpeDto.getAccessType()) ? CommonFunctions.addSpecification(specification, CPESpecifications.hasAccessType(searchCpeDto.getAccessType())) : specification;
        specification = StringUtils.isNotBlank(searchCpeDto.getExternalNumber()) ? CommonFunctions.addSpecification(specification, CPESpecifications.externalNumberStartWith(searchCpeDto.getExternalNumber())) : specification;
        specification = StringUtils.isNotBlank(searchCpeDto.getBatchNumber()) ? CommonFunctions.addSpecification(specification, CPESpecifications.hasBatchNumber(searchCpeDto.getBatchNumber())) : specification;
        specification = StringUtils.isNotBlank(searchCpeDto.getOrderId()) ? CommonFunctions.addSpecification(specification, CPESpecifications.hasOrderId(searchCpeDto.getOrderId())) : specification;
        specification = Objects.nonNull(searchCpeDto.getOrder()) ? CommonFunctions.addSpecificationWithCheck(specification, CPESpecifications.hasOrder(), CPESpecifications.hasNoOrder(), searchCpeDto.getOrder()) : specification;
        specification = StringUtils.isNotBlank(searchCpeDto.getServiceId()) ? CommonFunctions.addSpecification(specification, CPESpecifications.hasServiceId(searchCpeDto.getServiceId())) : specification;
        specification = Objects.nonNull(searchCpeDto.getService()) ? CommonFunctions.addSpecificationWithCheck(specification, CPESpecifications.hasService(), CPESpecifications.hasNoService(), searchCpeDto.getService()) : specification;
        specification = StringUtils.isNotBlank(searchCpeDto.getMacAddressLan()) ? CommonFunctions.addSpecification(specification, CPESpecifications.hasMacAddressLan(searchCpeDto.getMacAddressLan())) : specification;
        specification = StringUtils.isNotBlank(searchCpeDto.getMacAddress5G()) ? CommonFunctions.addSpecification(specification, CPESpecifications.hasMacAddress5G(searchCpeDto.getMacAddress5G())) : specification;
        specification = StringUtils.isNotBlank(searchCpeDto.getMacAddress4G()) ? CommonFunctions.addSpecification(specification, CPESpecifications.hasMacAddress24G(searchCpeDto.getMacAddress4G())) : specification;
        specification = StringUtils.isNotBlank(searchCpeDto.getHwVersion()) ? CommonFunctions.addSpecification(specification, CPESpecifications.hasHwVersion(searchCpeDto.getHwVersion())) : specification;
        specification = StringUtils.isNotBlank(searchCpeDto.getMacAddress()) ? CommonFunctions.addSpecification(specification, CPESpecifications.hasMacAddress(searchCpeDto.getMacAddress())) : specification;

        return specification;
    }

}

package mc.monacotelecom.tecrep.equipments.process.cpe;

import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.inventory.common.sm.StatusChanger;
import mc.monacotelecom.tecrep.equipments.assembler.CPEResourceAssembler;
import mc.monacotelecom.tecrep.equipments.dto.CPEDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.CPEDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.CPE;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmNotFoundException;
import mc.monacotelecom.tecrep.equipments.mapper.CPEMapper;
import mc.monacotelecom.tecrep.equipments.projections.CpeUnmProjection;
import mc.monacotelecom.tecrep.equipments.repository.CPERepository;
import mc.monacotelecom.tecrep.equipments.state.StateMachineService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.CPE_NOT_FOUND;

@Component
public class CPERetriever {
    private final CPERepository cpeRepository;
    private final CPEResourceAssembler cpeResourceAssembler;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final StateMachineService stateMachineService;
    private final CPEMapper cpeMapper;

    public CPERetriever(final CPERepository cpeRepository,
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
    public CPEDTO getByIdV1(Long cpeId) {
        return mapWithActionsV1(cpeRepository.findById(cpeId)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, CPE_NOT_FOUND, cpeId)));
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    private CPEDTO mapWithActionsV1(CPE cpe) {
        var cpeDto = cpeResourceAssembler.toModel(cpe);
        StatusChanger<Status, Event> statusEventStatusChanger = stateMachineService.getMachine(cpe.getRecyclable());
        List<Event> serviceTransitions = new ArrayList<>(statusEventStatusChanger.getAvailableEventsWithId(String.valueOf(cpe.getEquipmentId()), cpe.getStatus()));
        cpeDto.setEvents(serviceTransitions);
        return cpeDto;
    }

    public CPEDTOV2 getById(Long cpeId) {
        return mapWithActions(cpeRepository.findById(cpeId)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, CPE_NOT_FOUND, cpeId)));
    }

    private CPEDTOV2 mapWithActions(CPE cpe) {
        var cpeDto = cpeMapper.toDtoV2(cpe);
        StatusChanger<Status, Event> statusEventStatusChanger = stateMachineService.getMachine(cpe.getRecyclable());
        List<Event> serviceTransitions = new ArrayList<>(statusEventStatusChanger.getAvailableEventsWithId(String.valueOf(cpe.getEquipmentId()), cpe.getStatus()));
        cpeDto.setEvents(serviceTransitions);
        return cpeDto;
    }

    public List<CpeUnmProjection> getCpeIdsIn(List<Long> ids) {
        return cpeRepository.findAllByEquipmentIdIn(ids);
    }
}

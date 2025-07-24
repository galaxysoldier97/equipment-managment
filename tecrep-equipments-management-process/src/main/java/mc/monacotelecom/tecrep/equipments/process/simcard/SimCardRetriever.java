package mc.monacotelecom.tecrep.equipments.process.simcard;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.inventory.common.sm.StatusChanger;
import mc.monacotelecom.tecrep.equipments.dto.SimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.SimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentCategory;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmNotFoundException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.mapper.SimCardMapper;
import mc.monacotelecom.tecrep.equipments.repository.SimCardRepository;
import mc.monacotelecom.tecrep.equipments.state.StateMachineService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.*;

@Component
@RequiredArgsConstructor
public class SimCardRetriever {

    private final SimCardRepository simCardRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final StateMachineService stateMachineService;
    private final SimCardMapper simCardMapper;

    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO getByIdV1(Long simCardId) {
        return mapWithActionsV1(simCardRepository.findByEquipmentId(simCardId)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, SIMCARD_NOT_FOUND_ID, String.valueOf(simCardId))));
    }

    public SimCardDTOV2 getById(Long simCardId) {
        return mapWithActions(simCardRepository.findByEquipmentId(simCardId)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, SIMCARD_NOT_FOUND_ID, String.valueOf(simCardId))));
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO getByICCIDV1(String iccid) {
        return mapWithActionsV1(simCardRepository.findBySerialNumberAndCategory(iccid, EquipmentCategory.SIMCARD)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, SIMCARD_NOT_FOUND_ICCID, iccid)));
    }

    public SimCardDTOV2 getByIccid(String iccid) {
        return mapWithActions(simCardRepository.findBySerialNumberAndCategory(iccid, EquipmentCategory.SIMCARD)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, SIMCARD_NOT_FOUND_ICCID, iccid)));
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO getByIMSIV1(String imsi) {
        return mapWithActionsV1(simCardRepository.findByImsiNumber(imsi)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, SIMCARD_NOT_FOUND_IMSI, imsi)));
    }

    public SimCardDTOV2 getByIMSI(String imsi) {
        return mapWithActions(simCardRepository.findByImsiNumber(imsi)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, SIMCARD_NOT_FOUND_IMSI, imsi)));
    }

    private SimCardDTO mapWithActionsV1(SimCard simCard) {
        var simCardDTO = simCardMapper.toDtoV1(simCard);

        // Initialize recyclable context
        StatusChanger<Status, Event> statusEventStatusChanger = stateMachineService.getMachine(simCard.getRecyclable());
        List<Event> serviceTransitions = new ArrayList<>(statusEventStatusChanger.getAvailableEventsWithId(String.valueOf(simCardDTO.getEquipmentId()), simCardDTO.getStatus()));
        simCardDTO.setEvents(serviceTransitions);
        return simCardDTO;
    }

    private SimCardDTOV2 mapWithActions(SimCard simCard) {
        var simCardDTO = simCardMapper.toDtoV2(simCard);

        // Initialize recyclable context
        StatusChanger<Status, Event> statusEventStatusChanger = stateMachineService.getMachine(simCard.getRecyclable());
        List<Event> serviceTransitions = new ArrayList<>(statusEventStatusChanger.getAvailableEventsWithId(String.valueOf(simCardDTO.getId()), simCardDTO.getStatus()));
        simCardDTO.setEvents(serviceTransitions);
        return simCardDTO;
    }

    public Collection<Event> getAvailableEvents(Long simCardId) {
        SimCard simCard = simCardRepository.findById(simCardId)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, SIMCARD_NOT_FOUND_ID, String.valueOf(simCardId)));
        StatusChanger<Status, Event> statusEventStatusChanger = stateMachineService.getMachine(simCard.getRecyclable());
        return statusEventStatusChanger.getAvailableEventsWithId(String.valueOf(simCard.getEquipmentId()), simCard.getStatus());
    }
}

package mc.monacotelecom.tecrep.equipments.state;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.inventory.common.sm.StatusChanger;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.process.EquipmentStateMachineWithRecyclingService;
import mc.monacotelecom.tecrep.equipments.process.EquipmentStateMachineWithoutRecyclingService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StateMachineService {
    private final EquipmentStateMachineWithoutRecyclingService equipmentStateMachineWithoutRecyclingService;
    private final EquipmentStateMachineWithRecyclingService equipmentStateMachineWithRecyclingService;

    public StatusChanger<Status, Event> getMachine(Boolean isRecyclable) {
        return Boolean.TRUE.equals(isRecyclable) ? equipmentStateMachineWithRecyclingService : equipmentStateMachineWithoutRecyclingService;
    }
}

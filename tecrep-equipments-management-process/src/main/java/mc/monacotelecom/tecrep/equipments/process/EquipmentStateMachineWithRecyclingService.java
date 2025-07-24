package mc.monacotelecom.tecrep.equipments.process;

import mc.monacotelecom.inventory.common.Lambdas;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.inventory.common.sm.StatusChanger;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.SIMCARD_LIFECYCLE_ERROR;

@Component
public class EquipmentStateMachineWithRecyclingService implements StatusChanger<Status, Event> {

    private final StateMachineFactory<Status, Event> stateMachineFactory;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    public EquipmentStateMachineWithRecyclingService(final @Qualifier("equipmentStateMachineWithRecycling") StateMachineFactory<Status, Event> stateMachineFactory,
                                                     final LocalizedMessageBuilder localizedMessageBuilder) {
        this.stateMachineFactory = stateMachineFactory;
        this.localizedMessageBuilder = localizedMessageBuilder;
    }

    @Override
    public Status updateStatus(final String id, final Status initialStatus, final Event event) {
        final Status updatedStatus = this.changeStatus(this.stateMachineFactory, id, initialStatus, event);
        Lambdas.verifyOrThrow.test(Objects.isNull(updatedStatus), new EqmValidationException(localizedMessageBuilder, SIMCARD_LIFECYCLE_ERROR, event, initialStatus));
        return updatedStatus;
    }

    @Override
    public Collection<Event> getAvailableEventsWithId(final String id, final Status status) {
        return this.getAvailableEvents(stateMachineFactory.getStateMachine(id), status);
    }
}

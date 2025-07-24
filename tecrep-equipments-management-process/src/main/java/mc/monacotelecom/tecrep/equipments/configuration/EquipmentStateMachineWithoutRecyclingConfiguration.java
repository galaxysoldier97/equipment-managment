package mc.monacotelecom.tecrep.equipments.configuration;

import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachineFactory(name = "equipmentStateMachineWithoutRecycling")
public class EquipmentStateMachineWithoutRecyclingConfiguration extends StateMachineConfigurerAdapter<Status, Event> {
    @Override
    public void configure(StateMachineStateConfigurer<Status, Event> states) throws Exception {
        states.withStates()
                .initial(Status.INSTORE)
                .state(Status.AVAILABLE)
                .state(Status.BOOKED)
                .state(Status.ASSIGNED)
                .state(Status.ACTIVATED)
                .state(Status.DEACTIVATED)
                .end(Status.FINAL);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<Status, Event> transitions) throws Exception {
        transitions
                .withExternal().source(Status.INSTORE).target(Status.AVAILABLE).event(Event.available)
                .and()
                .withExternal().source(Status.AVAILABLE).target(Status.INSTORE).event(Event.instore)
                .and()
                .withExternal().source(Status.AVAILABLE).target(Status.BOOKED).event(Event.book)
                .and()
                .withExternal().source(Status.BOOKED).target(Status.AVAILABLE).event(Event.free)
                .and()
                .withExternal().source(Status.BOOKED).target(Status.ASSIGNED).event(Event.assign)
                .and()
                .withExternal().source(Status.BOOKED).target(Status.ACTIVATED).event(Event.activate)
                .and()
                .withExternal().source(Status.BOOKED).target(Status.DEACTIVATED).event(Event.deactivate)
                .and()
                .withExternal().source(Status.ASSIGNED).target(Status.AVAILABLE).event(Event.unassign)
                .and()
                .withExternal().source(Status.ASSIGNED).target(Status.ACTIVATED).event(Event.activate)
                .and()
                .withExternal().source(Status.ASSIGNED).target(Status.DEACTIVATED).event(Event.deactivate)
                .and()
                .withExternal().source(Status.ACTIVATED).target(Status.DEACTIVATED).event(Event.deactivate)
                .and()
                .withExternal().source(Status.ACTIVATED).target(Status.BOOKED).event(Event.rollback_activate)
                .and()
                .withExternal().source(Status.DEACTIVATED).target(Status.ACTIVATED).event(Event.rollback_deactivate)
                .and()
                .withExternal().source(Status.DEACTIVATED).target(Status.AVAILABLE).event(Event.available);
    }
}
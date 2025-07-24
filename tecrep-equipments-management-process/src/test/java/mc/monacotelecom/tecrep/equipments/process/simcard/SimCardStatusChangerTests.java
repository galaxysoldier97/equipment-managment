package mc.monacotelecom.tecrep.equipments.process.simcard;

import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.dto.request.ChangeStatusDto;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentNature;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.exceptions.validation.NoServiceIdException;
import mc.monacotelecom.tecrep.equipments.mapper.SimCardMapper;
import mc.monacotelecom.tecrep.equipments.mapper.SimCardMapperImpl;
import mc.monacotelecom.tecrep.equipments.operator.OperatorService;
import mc.monacotelecom.tecrep.equipments.repository.SimCardRepository;
import mc.monacotelecom.tecrep.equipments.state.StateMachineService;
import mc.monacotelecom.tecrep.equipments.utils.AuditCleaner;
import org.hibernate.envers.DefaultRevisionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.envers.repository.support.DefaultRevisionMetadata;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.SIMCARD_ALREADY_MAIN_FOR_SERVICE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class SimCardStatusChangerTests {
    private SimCardStatusChanger simCardStatusChanger;

    @Mock
    private SimCardRepository simCardRepository;

    @Mock
    private LocalizedMessageBuilder localizedMessageBuilder;

    @Mock
    private StateMachineService stateMachineService;

    @Mock
    private OperatorService operatorService;

    @Mock
    private AuditCleaner auditCleaner;

    private final SimCardMapper simCardMapper = new SimCardMapperImpl();

    private final Clock clock = Clock.systemDefaultZone();

    @BeforeEach
    public void setup() {
        initMocks(this);
        this.simCardStatusChanger = new SimCardStatusChanger(simCardRepository, localizedMessageBuilder, stateMachineService, auditCleaner, operatorService, clock, simCardMapper);
    }

    @Nested
    @DisplayName("Rollback API")
    class Rollback {

        @Test
        void rollbackActivate_success_noAuditData() {
            SimCard simCard = getSimCard(1L, Status.ACTIVATED);
            simCard.setServiceId(123L);

            when(simCardRepository.findRevisions(simCard.getEquipmentId())).thenReturn(Revisions.of(Collections.emptyList()));

            simCardStatusChanger.updateAttributesForRollback(simCard, Event.rollback_activate);

            assertNull(simCard.getServiceId());
        }

        @Test
        void rollbackActivate_success_singleAuditData_noOrderId() {
            SimCard simCard = getSimCard(1L, Status.ACTIVATED);
            simCard.setServiceId(123L);

            var revisions = Revisions.of(List.of(
                    getSimCardRevision(1, Status.ASSIGNED, "2020-05-01T06:30:00", null, null)));
            when(simCardRepository.findRevisions(simCard.getEquipmentId())).thenReturn(revisions);

            simCardStatusChanger.updateAttributesForRollback(simCard, Event.rollback_activate);

            assertNull(simCard.getServiceId());
        }

        @Test
        void rollbackActivate_success_singleAuditData_withOrderId() {
            SimCard simCard = getSimCard(1L, Status.ACTIVATED);
            simCard.setServiceId(123L);

            var revisions = Revisions.of(List.of(
                    getSimCardRevision(1, Status.ASSIGNED, "2020-05-01T06:30:00", "ORDER", null)));
            when(simCardRepository.findRevisions(simCard.getEquipmentId())).thenReturn(revisions);

            simCardStatusChanger.updateAttributesForRollback(simCard, Event.rollback_activate);

            assertAll(
                    () -> assertNull(simCard.getServiceId()),
                    () -> assertEquals("ORDER", simCard.getOrderId())
            );
        }

        @Test
        void rollbackActivate_success_multipleAuditData_withOrderId() {
            SimCard simCard = getSimCard(1L, Status.ACTIVATED);
            simCard.setServiceId(123L);

            var revisions = Revisions.of(List.of(
                    getSimCardRevision(1, Status.DEACTIVATED, "2018-04-01T07:00:00", "ORDER1", null),
                    getSimCardRevision(3, Status.ASSIGNED, "2020-05-01T06:30:00", "ORDER3", null),
                    getSimCardRevision(2, Status.AVAILABLE, "2019-04-01T05:00:00", "ORDER2", null)));
            when(simCardRepository.findRevisions(simCard.getEquipmentId())).thenReturn(revisions);

            simCardStatusChanger.updateAttributesForRollback(simCard, Event.rollback_activate);

            assertAll(
                    () -> assertNull(simCard.getServiceId()),
                    () -> assertEquals("ORDER3", simCard.getOrderId())
            );
        }

        @Test
        void rollbackDeactivate_success_noAuditData() {
            SimCard simCard = getSimCard(1L, Status.DEACTIVATED);

            when(simCardRepository.findRevisions(simCard.getEquipmentId())).thenReturn(Revisions.of(Collections.emptyList()));

            simCardStatusChanger.updateAttributesForRollback(simCard, Event.rollback_deactivate);

            assertNull(simCard.getServiceId());
        }

        @Test
        void rollbackDeactivate_success_singleAuditData_noServiceId() {
            SimCard simCard = getSimCard(1L, Status.DEACTIVATED);

            var revisions = Revisions.of(List.of(
                    getSimCardRevision(1, Status.ASSIGNED, "2020-05-01T06:30:00", null, null)));
            when(simCardRepository.findRevisions(simCard.getEquipmentId())).thenReturn(revisions);

            simCardStatusChanger.updateAttributesForRollback(simCard, Event.rollback_deactivate);

            assertNull(simCard.getServiceId());
        }

        @Test
        void rollbackDeactivate_success_singleAuditData_withServiceId() {
            SimCard simCard = getSimCard(1L, Status.DEACTIVATED);

            var revisions = Revisions.of(List.of(
                    getSimCardRevision(1, Status.ASSIGNED, "2020-05-01T06:30:00", null, 123L)));
            when(simCardRepository.findRevisions(simCard.getEquipmentId())).thenReturn(revisions);

            simCardStatusChanger.updateAttributesForRollback(simCard, Event.rollback_deactivate);

            assertEquals((Long) 123L, simCard.getServiceId());
        }

        @Test
        void rollbackDeactivate_success_multipleAuditData_withServiceId() {
            SimCard simCard = getSimCard(1L, Status.DEACTIVATED);

            var revisions = Revisions.of(List.of(
                    getSimCardRevision(1, Status.DEACTIVATED, "2018-04-01T07:00:00", null, 789L),
                    getSimCardRevision(3, Status.ASSIGNED, "2020-05-01T06:30:00", null, 123L),
                    getSimCardRevision(2, Status.AVAILABLE, "2019-04-01T05:00:00", null, 456L)));
            when(simCardRepository.findRevisions(simCard.getEquipmentId())).thenReturn(revisions);

            simCardStatusChanger.updateAttributesForRollback(simCard, Event.rollback_deactivate);

            assertEquals((Long) 123L, simCard.getServiceId());
        }

        @Test
        void rollbackDeactivate_failure_alreadyMainSimForServiceId() {
            SimCard simCard = getSimCard(1L, Status.ACTIVATED);
            simCard.setServiceId(123L);
            simCard.setNature(EquipmentNature.MAIN);

            var revisions = Revisions.of(List.of(
                    getSimCardRevision(1, Status.DEACTIVATED, "2018-04-01T07:00:00", null, 789L),
                    getSimCardRevision(3, Status.ASSIGNED, "2020-05-01T06:30:00", null, 123L),
                    getSimCardRevision(2, Status.AVAILABLE, "2019-04-01T05:00:00", null, 456L)));
            when(simCardRepository.findRevisions(simCard.getEquipmentId())).thenReturn(revisions);

            when(simCardRepository.findByServiceId(123L)).thenReturn(List.of(getSimCard(2L, Status.ACTIVATED)));

            var e = assertThrows(EqmValidationException.class, () -> simCardStatusChanger.updateAttributesForRollback(simCard, Event.rollback_deactivate));
            assertEquals(SIMCARD_ALREADY_MAIN_FOR_SERVICE, e.getMessageKey());
        }
    }

    @Nested
    @DisplayName("Update Assign/Activate")
    class UpdateAssignActivate {
        @Test
        void updateAttributesForAssignActivate_noServiceId() {
            SimCard simCard = getSimCard(1L, Status.AVAILABLE);

            final var changeStatusDto = new ChangeStatusDto();

            assertThrows(NoServiceIdException.class, () -> simCardStatusChanger.updateAttributesForAssignActivate(simCard, Event.assign, changeStatusDto));
        }

        @Test
        void updateAttributesForAssignActivate_noServiceIdForEir() {
            SimCard simCard = getSimCard(1L, Status.AVAILABLE);
            simCard.setOrderId("ORDER");
            when(operatorService.isEir()).thenReturn(true);

            final var changeStatusDto = new ChangeStatusDto();

            simCardStatusChanger.updateAttributesForAssignActivate(simCard, Event.assign, changeStatusDto);

            assertNull(simCard.getOrderId());
        }

        @Test
        void updateAttributesForAssignActivate_withServiceId() {
            SimCard simCard = getSimCard(1L, Status.AVAILABLE);
            simCard.setOrderId("ORDER");

            final var changeStatusDto = new ChangeStatusDto();
            changeStatusDto.setServiceId(123L);

            simCardStatusChanger.updateAttributesForAssignActivate(simCard, Event.assign, changeStatusDto);

            assertNull(simCard.getOrderId());
            assertEquals((Long) 123L, simCard.getServiceId());
        }

        @Test
        void updateAttributesForAssignActivate_withServiceId_addMainWithExistingMain() {
            SimCard simCard = getSimCard(1L, Status.AVAILABLE);
            simCard.setOrderId("ORDER");

            final var changeStatusDto = new ChangeStatusDto();
            changeStatusDto.setServiceId(123L);

            when(simCardRepository.findByServiceId(123L))
                    .thenReturn(List.of(getSimCard(2L, Status.ACTIVATED)));

            var e = assertThrows(EqmValidationException.class, () -> simCardStatusChanger.updateAttributesForAssignActivate(simCard, Event.assign, changeStatusDto));
            assertEquals(SIMCARD_ALREADY_MAIN_FOR_SERVICE, e.getMessageKey());

            verify(simCardRepository).findByServiceId(123L);
        }

        @Test
        void updateAttributesForAssignActivate_withServiceId_addMainWithExistingMainSelf() {
            SimCard simCard = getSimCard(1L, Status.AVAILABLE);
            simCard.setOrderId("ORDER");

            final var changeStatusDto = new ChangeStatusDto();
            changeStatusDto.setServiceId(123L);

            when(simCardRepository.findByServiceId(123L))
                    .thenReturn(List.of(simCard));

            simCardStatusChanger.updateAttributesForAssignActivate(simCard, Event.assign, changeStatusDto);

            verify(simCardRepository).findByServiceId(123L);
        }

        @Test
        void updateAttributesForAssignActivate_withServiceId_addMainWithExistingAdditional() {
            SimCard simCard = getSimCard(1L, Status.AVAILABLE);
            simCard.setOrderId("ORDER");

            final var changeStatusDto = new ChangeStatusDto();
            changeStatusDto.setServiceId(123L);

            SimCard otherSim = getSimCard(2L, Status.ACTIVATED);
            otherSim.setNature(EquipmentNature.ADDITIONAL);
            when(simCardRepository.findByServiceId(123L))
                    .thenReturn(List.of(otherSim));

            simCardStatusChanger.updateAttributesForAssignActivate(simCard, Event.assign, changeStatusDto);

            verify(simCardRepository).findByServiceId(123L);
        }

        @Test
        void updateAttributesForAssignActivate_withServiceId_addAdditional() {
            SimCard simCard = getSimCard(1L, Status.AVAILABLE);
            simCard.setOrderId("ORDER");
            simCard.setNature(EquipmentNature.ADDITIONAL);

            final var changeStatusDto = new ChangeStatusDto();
            changeStatusDto.setServiceId(123L);

            simCardStatusChanger.updateAttributesForAssignActivate(simCard, Event.assign, changeStatusDto);

            verify(simCardRepository, times(0)).findByServiceId(123L);
        }

        @Test
        void updateAttributesForAssignActivate_withServiceIdAlreadyUsedBySameService() {
            SimCard simCard = getSimCard(1L, Status.ASSIGNED);
            simCard.setOrderId("ORDER");

            final var changeStatusDto = new ChangeStatusDto();
            changeStatusDto.setServiceId(123L);

            when(simCardRepository.findByServiceId(123L))
                    .thenReturn(List.of(getSimCard(1L, Status.ASSIGNED)));

            simCardStatusChanger.updateAttributesForAssignActivate(simCard, Event.assign, changeStatusDto);

            assertNull(simCard.getOrderId());
            assertEquals((Long) 123L, simCard.getServiceId());
        }
    }

    private static SimCard getSimCard(Long id, Status status) {
        var simCard = new SimCard();
        simCard.setEquipmentId(id);
        simCard.setStatus(status);
        simCard.setNature(EquipmentNature.MAIN);
        return simCard;
    }

    private static Revision<Integer, SimCard> getSimCardRevision(int revId, Status status, String dateTime, String orderId, Long serviceId) {
        SimCard simCard = getSimCard(1L, status);
        simCard.setOrderId(orderId);
        simCard.setServiceId(serviceId);

        DefaultRevisionEntity revisionEntityDeactivated = new DefaultRevisionEntity();
        revisionEntityDeactivated.setId(revId);
        revisionEntityDeactivated.setTimestamp(LocalDateTime.parse(dateTime).toInstant(ZoneOffset.UTC).toEpochMilli());
        return Revision.of(new DefaultRevisionMetadata(revisionEntityDeactivated), simCard);
    }
}

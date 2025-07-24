package mc.monacotelecom.tecrep.equipments.process.simcard;

import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateSimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentCategory;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentNature;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmConflictException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.mapper.SimCardMapper;
import mc.monacotelecom.tecrep.equipments.mapper.SimCardMapperImpl;
import mc.monacotelecom.tecrep.equipments.operator.OperatorService;
import mc.monacotelecom.tecrep.equipments.repository.SimCardRepository;
import mc.monacotelecom.tecrep.equipments.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class SimCardUpdaterTests {
    private SimCardUpdater simCardUpdater;

    @Mock
    private SimCardRepository simCardRepository;

    @Mock
    private LocalizedMessageBuilder localizedMessageBuilder;

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private OperatorService operatorService;

    private final SimCardMapper simCardMapper = new SimCardMapperImpl();

    @BeforeEach
    public void setup() {
        initMocks(this);
        this.simCardUpdater = new SimCardUpdater(simCardRepository, localizedMessageBuilder, warehouseRepository, operatorService, simCardMapper);
    }

    @Nested
    @DisplayName("Validate Attributes API")
    class ValidateAttributes {
        @Test
        void validateAttributes_ok() {
            var dto = new UpdateSimCardDTOV2();
            dto.setSerialNumber("987654321234567864");
            dto.setPackId("456");
            dto.setImsiNumber("123456789876521");
            dto.setImsiSponsorNumber("123456789876597");

            var simCard = new SimCard();
            simCard.setEquipmentId(1L);
            simCard.setSerialNumber("987654321234567898");
            simCard.setPackId("123");
            simCard.setImsiNumber("123456789876543");
            simCard.setImsiSponsorNumber("123456789876543");

            simCardUpdater.validateAttributes(dto, simCard);

            verify(simCardRepository, times(1)).findBySerialNumberAndCategory("987654321234567864", EquipmentCategory.SIMCARD);
            verify(simCardRepository, times(1)).findByPackId("456");
            verify(simCardRepository, times(1)).findByImsiNumber("123456789876521");
            verify(simCardRepository, times(1)).findByImsiSponsorNumber("123456789876597");
        }

        @Test
        void validateAttributes_serialNumber_conflictButSameSim() {
            var dto = new UpdateSimCardDTOV2();
            dto.setSerialNumber("987654321234567864");

            var simCard = new SimCard();
            simCard.setEquipmentId(1L);
            simCard.setSerialNumber("987654321234567864");

            when(simCardRepository.findBySerialNumberAndCategory("987654321234567864", EquipmentCategory.SIMCARD)).thenReturn(Optional.of(simCard));

            simCardUpdater.validateAttributes(dto, simCard);

            verify(simCardRepository, times(1)).findBySerialNumberAndCategory("987654321234567864", EquipmentCategory.SIMCARD);
        }

        @Test
        void validateAttributes_serialNumber_conflict() {
            var dto = new UpdateSimCardDTOV2();
            dto.setSerialNumber("987654321234567865");

            var simCard = new SimCard();
            simCard.setEquipmentId(1L);
            simCard.setSerialNumber("987654321234567864");

            var otherSimCard = new SimCard();
            otherSimCard.setEquipmentId(56L);
            when(simCardRepository.findBySerialNumberAndCategory("987654321234567865", EquipmentCategory.SIMCARD)).thenReturn(Optional.of(otherSimCard));

            var e = assertThrows(EqmConflictException.class, () -> simCardUpdater.validateAttributes(dto, simCard));
            assertEquals(SIMCARD_SERIAL_NUMBER_ALREADY_IN_USE, e.getMessageKey());

            verify(simCardRepository, times(1)).findBySerialNumberAndCategory("987654321234567865", EquipmentCategory.SIMCARD);
        }

        @Test
        void validateAttributes_imsiNumber_conflictButSameSim() {
            var dto = new UpdateSimCardDTOV2();
            dto.setImsiNumber("123456789876543");

            var simCard = new SimCard();
            simCard.setEquipmentId(1L);
            simCard.setImsiNumber("123456789876543");

            when(simCardRepository.findByImsiNumber("123456789876543")).thenReturn(Optional.of(simCard));

            simCardUpdater.validateAttributes(dto, simCard);

            verify(simCardRepository, times(1)).findByImsiNumber("123456789876543");
        }

        @Test
        void validateAttributes_imsiNumber_conflict() {
            var dto = new UpdateSimCardDTOV2();
            dto.setImsiNumber("123456789876544");

            var simCard = new SimCard();
            simCard.setEquipmentId(1L);
            simCard.setImsiNumber("123456789876543");

            var otherSimCard = new SimCard();
            otherSimCard.setEquipmentId(56L);
            when(simCardRepository.findByImsiNumber("123456789876544")).thenReturn(Optional.of(otherSimCard));

            var e = assertThrows(EqmConflictException.class, () -> simCardUpdater.validateAttributes(dto, simCard));
            assertEquals(SIMCARD_IMSI_ALREADY_IN_USE, e.getMessageKey());

            verify(simCardRepository, times(1)).findByImsiNumber("123456789876544");
        }

        @Test
        void validateAttributes_imsiSponsorNumber_conflictButSameSim() {
            var dto = new UpdateSimCardDTOV2();
            dto.setImsiSponsorNumber("123456789876543");

            var simCard = new SimCard();
            simCard.setEquipmentId(1L);
            simCard.setImsiSponsorNumber("123456789876543");

            when(simCardRepository.findByImsiSponsorNumber("123456789876543")).thenReturn(Optional.of(simCard));

            simCardUpdater.validateAttributes(dto, simCard);

            verify(simCardRepository, times(1)).findByImsiSponsorNumber("123456789876543");
        }

        @Test
        void validateAttributes_imsiSponsorNumber_conflict() {
            var dto = new UpdateSimCardDTOV2();
            dto.setImsiSponsorNumber("123456789876544");

            var simCard = new SimCard();
            simCard.setEquipmentId(1L);
            simCard.setImsiSponsorNumber("123456789876543");

            var otherSimCard = new SimCard();
            otherSimCard.setEquipmentId(56L);
            when(simCardRepository.findByImsiSponsorNumber("123456789876544")).thenReturn(Optional.of(otherSimCard));

            var e = assertThrows(EqmConflictException.class, () -> simCardUpdater.validateAttributes(dto, simCard));
            assertEquals(SIMCARD_IMSI_SPONSOR_NUMBER_ALREADY_IN_USE, e.getMessageKey());

            verify(simCardRepository, times(1)).findByImsiSponsorNumber("123456789876544");
        }

        @Test
        void validateAttributes_packId_conflictButSameSim() {
            var dto = new UpdateSimCardDTOV2();
            dto.setPackId("123");

            var simCard = new SimCard();
            simCard.setEquipmentId(1L);
            simCard.setPackId("123");

            when(simCardRepository.findByPackId("123")).thenReturn(Optional.of(simCard));

            simCardUpdater.validateAttributes(dto, simCard);

            verify(simCardRepository, times(1)).findByPackId("123");
        }

        @Test
        void validateAttributes_packId_conflict() {
            var dto = new UpdateSimCardDTOV2();
            dto.setPackId("456");

            var simCard = new SimCard();
            simCard.setEquipmentId(1L);
            simCard.setPackId("123");

            var otherSimCard = new SimCard();
            otherSimCard.setEquipmentId(56L);
            when(simCardRepository.findByPackId("456")).thenReturn(Optional.of(otherSimCard));

            var e = assertThrows(EqmConflictException.class, () -> simCardUpdater.validateAttributes(dto, simCard));
            assertEquals(SIMCARD_PACK_ID_ALREADY_IN_USE, e.getMessageKey());

            verify(simCardRepository, times(1)).findByPackId("456");
        }
    }

    @Nested
    @DisplayName("Validate API")
    class Validate {

        @ParameterizedTest
        @ValueSource(strings = {"AVAILABLE", "INSTORE", "ONHOLD"})
        void withServiceId_badStatus(Status currentStatus) {
            var dto = new UpdateSimCardDTOV2();
            dto.setServiceId(456L);

            var simCard = new SimCard();
            simCard.setEquipmentId(1L);
            simCard.setStatus(currentStatus);
            simCard.setNature(EquipmentNature.MAIN);

            var e = assertThrows(EqmValidationException.class, () -> simCardUpdater.commonValidate(dto, simCard));
            assertEquals(WRONG_EQUIPMENT_STATUS, e.getMessageKey());
        }

        @ParameterizedTest
        @ValueSource(strings = {"ASSIGNED", "ACTIVATED", "BOOKED"})
        void withServiceId_success_noExisting(Status currentStatus) {
            var dto = new UpdateSimCardDTOV2();
            dto.setServiceId(456L);

            var simCard = new SimCard();
            simCard.setServiceId(456L);
            simCard.setEquipmentId(1L);
            simCard.setStatus(currentStatus);
            simCard.setNature(EquipmentNature.MAIN);

            simCardUpdater.commonValidate(dto, simCard);

            verify(simCardRepository, times(1)).findByServiceId(456L);
        }

        @Test
        void withServiceId_failure_bookedWithoutServiceId() {
            var dto = new UpdateSimCardDTOV2();
            dto.setServiceId(456L);

            var simCard = new SimCard();
            simCard.setEquipmentId(1L);
            simCard.setStatus(Status.BOOKED);
            simCard.setNature(EquipmentNature.MAIN);

            var e = assertThrows(EqmValidationException.class, () -> simCardUpdater.commonValidate(dto, simCard));
            assertEquals(SIMCARD_SERVICE_STATUS_UPDATE_ASSIGNED_OR_ACTIVATED_VALIDATION, e.getMessageKey());

            verify(simCardRepository, times(1)).findByServiceId(456L);
        }

        @Test
        void withServiceId_failure_addMainWithExistingMain() {
            var dto = new UpdateSimCardDTOV2();
            dto.setServiceId(456L);

            var simCard = new SimCard();
            simCard.setEquipmentId(1L);
            simCard.setStatus(Status.ACTIVATED);
            simCard.setNature(EquipmentNature.MAIN);

            var otherSim = new SimCard();
            otherSim.setEquipmentId(99L);
            otherSim.setServiceId(456L);
            otherSim.setNature(EquipmentNature.MAIN);
            when(simCardRepository.findByServiceId(456L)).thenReturn(List.of(otherSim));

            var e = assertThrows(EqmValidationException.class, () -> simCardUpdater.commonValidate(dto, simCard));
            assertEquals(SIMCARD_ALREADY_MAIN_FOR_SERVICE, e.getMessageKey());

            verify(simCardRepository, times(1)).findByServiceId(456L);
        }

        @Test
        void withServiceId_success_addMainWithExistingMainSelf() {
            var dto = new UpdateSimCardDTOV2();
            dto.setServiceId(456L);

            var simCard = new SimCard();
            simCard.setEquipmentId(1L);
            simCard.setStatus(Status.ACTIVATED);
            simCard.setNature(EquipmentNature.MAIN);

            when(simCardRepository.findByServiceId(456L)).thenReturn(List.of(simCard));

            simCardUpdater.commonValidate(dto, simCard);

            verify(simCardRepository, times(1)).findByServiceId(456L);
        }

        @Test
        void withServiceId_success_addMainWithExistingAdditional() {
            var dto = new UpdateSimCardDTOV2();
            dto.setServiceId(456L);

            var simCard = new SimCard();
            simCard.setEquipmentId(1L);
            simCard.setStatus(Status.ACTIVATED);
            simCard.setNature(EquipmentNature.MAIN);

            var otherSim = new SimCard();
            otherSim.setEquipmentId(99L);
            otherSim.setServiceId(456L);
            otherSim.setNature(EquipmentNature.ADDITIONAL);
            when(simCardRepository.findByServiceId(456L)).thenReturn(List.of(otherSim));

            simCardUpdater.commonValidate(dto, simCard);

            verify(simCardRepository, times(1)).findByServiceId(456L);
        }

        @Test
        void withServiceId_success_addAdditionalWithExistingMain() {
            var dto = new UpdateSimCardDTOV2();
            dto.setServiceId(456L);

            var simCard = new SimCard();
            simCard.setEquipmentId(1L);
            simCard.setStatus(Status.ACTIVATED);
            simCard.setNature(EquipmentNature.ADDITIONAL);

            simCardUpdater.commonValidate(dto, simCard);

            verify(simCardRepository, times(0)).findByServiceId(456L);
        }

        @Test
        void withServiceId_success_alreadyExistingButSame() {
            var dto = new UpdateSimCardDTOV2();
            dto.setServiceId(456L);

            var simCard = new SimCard();
            simCard.setEquipmentId(1L);
            simCard.setStatus(Status.ACTIVATED);
            simCard.setNature(EquipmentNature.MAIN);

            when(simCardRepository.findByServiceId(456L)).thenReturn(List.of(simCard));

            simCardUpdater.commonValidate(dto, simCard);

            verify(simCardRepository, times(1)).findByServiceId(456L);
        }

        @ParameterizedTest
        @ValueSource(strings = {"ACTIVATED", "ASSIGNED"})
        void withoutServiceId_notEir_failureIfActivatedAssigned(Status currentStatus) {
            when(operatorService.isEir()).thenReturn(false);

            var dto = new UpdateSimCardDTOV2();

            var simCard = new SimCard();
            simCard.setEquipmentId(1L);
            simCard.setStatus(currentStatus);
            simCard.setNature(EquipmentNature.MAIN);

            var e = assertThrows(EqmValidationException.class, () -> simCardUpdater.commonValidate(dto, simCard));
            assertEquals(SIMCARD_SERVICE_NOT_NULL_STATUS_ASSIGNED_OR_ACTIVATED, e.getMessageKey());

            verifyNoInteractions(simCardRepository);
        }

        @Test
        void withImsi_badLength_failure() {
            var dto = new UpdateSimCardDTOV2();
            dto.setImsiNumber("456");

            var simCard = new SimCard();
            simCard.setEquipmentId(1L);
            simCard.setStatus(Status.AVAILABLE);
            simCard.setNature(EquipmentNature.MAIN);

            var e = assertThrows(EqmValidationException.class, () -> simCardUpdater.commonValidate(dto, simCard));
            assertEquals(SIMCARD_IMSI_LENGTH_VALIDATION, e.getMessageKey());
        }

        @Test
        void withImsi_success() {
            var dto = new UpdateSimCardDTOV2();
            dto.setImsiNumber("123456789123456");

            var simCard = new SimCard();
            simCard.setEquipmentId(1L);
            simCard.setStatus(Status.AVAILABLE);
            simCard.setNature(EquipmentNature.MAIN);

            assertDoesNotThrow(() -> simCardUpdater.commonValidate(dto, simCard));
        }

        @Test
        void withOrder_booked_success() {
            when(operatorService.isEir()).thenReturn(true);

            var dto = new UpdateSimCardDTOV2();
            dto.setOrderId("ABC");

            var simCard = new SimCard();
            simCard.setEquipmentId(1L);
            simCard.setStatus(Status.BOOKED);
            simCard.setNature(EquipmentNature.MAIN);

            assertDoesNotThrow(() -> simCardUpdater.commonValidate(dto, simCard));
        }

        @ParameterizedTest
        @ValueSource(strings = {"ACTIVATED", "ASSIGNED", "AVAILABLE"})
        void withOrder_notBooked_failure(Status currentStatus) {
            when(operatorService.isEir()).thenReturn(true);

            var dto = new UpdateSimCardDTOV2();
            dto.setOrderId("ABC");

            var simCard = new SimCard();
            simCard.setEquipmentId(1L);
            simCard.setStatus(currentStatus);
            simCard.setNature(EquipmentNature.MAIN);

            var e = assertThrows(EqmValidationException.class, () -> simCardUpdater.commonValidate(dto, simCard));
            assertEquals(SIMCARD_ORDERID_CHECK_STATUS_BOOKED, e.getMessageKey());
        }

        @Test
        void withMain_failure_addMainWithExistingMainForSameService() {
            var dto = new UpdateSimCardDTOV2();
            dto.setNature(EquipmentNature.MAIN);

            var simCard = new SimCard();
            simCard.setEquipmentId(1L);
            simCard.setStatus(Status.ACTIVATED);
            simCard.setNature(EquipmentNature.ADDITIONAL);
            simCard.setServiceId(456L);

            var otherSim = new SimCard();
            otherSim.setEquipmentId(99L);
            otherSim.setServiceId(456L);
            otherSim.setNature(EquipmentNature.MAIN);
            when(simCardRepository.findByServiceId(456L)).thenReturn(List.of(otherSim));

            var e = assertThrows(EqmValidationException.class, () -> simCardUpdater.commonValidate(dto, simCard));
            assertEquals(SIMCARD_ALREADY_MAIN_FOR_SERVICE, e.getMessageKey());

            verify(simCardRepository, times(1)).findByServiceId(456L);
        }

        @Test
        void withMain_success_addMainWithExistingMainSelf() {
            var dto = new UpdateSimCardDTOV2();
            dto.setNature(EquipmentNature.MAIN);

            var simCard = new SimCard();
            simCard.setEquipmentId(1L);
            simCard.setStatus(Status.ACTIVATED);
            simCard.setNature(EquipmentNature.MAIN);
            simCard.setServiceId(456L);

            when(simCardRepository.findByServiceId(456L)).thenReturn(List.of(simCard));

            simCardUpdater.commonValidate(dto, simCard);

            verify(simCardRepository, times(1)).findByServiceId(456L);
        }

        @Test
        void withMain_success_addMainWithExistingAdditional() {
            var dto = new UpdateSimCardDTOV2();
            dto.setNature(EquipmentNature.MAIN);

            var simCard = new SimCard();
            simCard.setEquipmentId(1L);
            simCard.setStatus(Status.ACTIVATED);
            simCard.setNature(EquipmentNature.ADDITIONAL);
            simCard.setServiceId(456L);

            var otherSim = new SimCard();
            otherSim.setEquipmentId(99L);
            otherSim.setServiceId(456L);
            otherSim.setNature(EquipmentNature.ADDITIONAL);
            when(simCardRepository.findByServiceId(456L)).thenReturn(List.of(otherSim));

            simCardUpdater.commonValidate(dto, simCard);

            verify(simCardRepository, times(1)).findByServiceId(456L);
        }

        @Test
        void withMainAndServiceId_success() {
            var dto = new UpdateSimCardDTOV2();
            dto.setNature(EquipmentNature.MAIN);
            dto.setServiceId(789L);

            var simCard = new SimCard();
            simCard.setEquipmentId(1L);
            simCard.setStatus(Status.ACTIVATED);
            simCard.setNature(EquipmentNature.ADDITIONAL);
            simCard.setServiceId(456L);

            var otherSim = new SimCard();
            otherSim.setEquipmentId(99L);
            otherSim.setServiceId(456L);
            otherSim.setNature(EquipmentNature.MAIN);
            when(simCardRepository.findByServiceId(456L)).thenReturn(List.of(otherSim));

            simCardUpdater.commonValidate(dto, simCard);

            verify(simCardRepository, times(1)).findByServiceId(789L);
        }
    }
}

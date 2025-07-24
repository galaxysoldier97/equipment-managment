package mc.monacotelecom.tecrep.equipments.importer.validators;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.Lambdas;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateAncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.entity.CPE;
import mc.monacotelecom.tecrep.equipments.entity.Equipment;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.repository.AncillaryRepository;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static mc.monacotelecom.tecrep.equipments.enums.Status.ACTIVATED;
import static mc.monacotelecom.tecrep.equipments.enums.Status.ASSIGNED;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AncillaryEquipmentValidator {

    private final AncillaryRepository ancillaryRepository;
    private final EquipmentRepository<Equipment> equipmentRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    private boolean hasUnwantedPairedEquipmentId(Boolean independent, Status status, Long pairedEquipmentId) {
        return Objects.nonNull(pairedEquipmentId) && Boolean.TRUE.equals(independent) && !List.of(ASSIGNED, ACTIVATED).contains(status);
    }

    public AncillaryEquipment checkBeforeUpdate(final Long id, UpdateAncillaryEquipment updateDTO) {
        AncillaryEquipment currentAncillaryEquipment = ancillaryRepository.findById(id)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, ANCILLARY_EQUIPMENT_NOT_FOUND, id));

        // MAC Address must not be used by another ancillary
        if (StringUtils.isNotBlank(updateDTO.getMacAddress())) {
            ancillaryRepository.findByMacAddress(updateDTO.getMacAddress()).ifPresent(foundAncillaryEquipment -> {
                if (!Objects.equals(foundAncillaryEquipment.getEquipmentId(), currentAncillaryEquipment.getEquipmentId())) {
                    throw new EqmValidationException(localizedMessageBuilder, EQUIPMENT_MAC_ADDRESS_ALREADY_IN_USE, updateDTO.getMacAddress());
                }
            });
        }

        // Order ID can be changed only if status is BOOKED
        if (StringUtils.isNoneBlank(updateDTO.getOrderId()) && !Status.BOOKED.equals(currentAncillaryEquipment.getStatus())) {
            throw new EqmValidationException(localizedMessageBuilder, ANCILLARY_ORDERID_CHECK_STATUS_BOOKED);
        }

        // Calculate if the ancillary is currently independent, of if the request makes it independent
        final boolean isIndependent = Boolean.TRUE.equals(updateDTO.getIndependent()) ||
                (Objects.isNull(updateDTO.getIndependent()) && Boolean.TRUE.equals(currentAncillaryEquipment.getIndependent()));

        // If not independent, paired equipment ID must be provided in request
        Lambdas.verifyOrThrow.test(!isIndependent && Objects.isNull(updateDTO.getPairedEquipmentId()),
                new EqmValidationException(localizedMessageBuilder, ANCILLARY_EQUIPMENT_PAIRED_EQUIPMENT_REQUIRED));

        // If currently independent and not ACTIVATED/ASSIGNED, paired equipment ID cannot be set
        Lambdas.verifyOrThrow.test(hasUnwantedPairedEquipmentId(updateDTO.getIndependent(), currentAncillaryEquipment.getStatus(), updateDTO.getPairedEquipmentId()),
                new EqmValidationException(localizedMessageBuilder, ANCILLARY_EQUIPMENT_PAIRED_EQUIPMENT_INDEPENDENT_HAS_TO_BE_NULL));

        // If serviceId is filled
        if (Objects.nonNull(updateDTO.getServiceId())) {
            // Must be independent and status ASSIGNED/ACTIVATED
            Lambdas.verifyOrThrow.test(!List.of(ASSIGNED, ACTIVATED).contains(currentAncillaryEquipment.getStatus()) || !isIndependent,
                    new EqmValidationException(localizedMessageBuilder, ANCILLARY_SERVICE_CANNOT_BE_UPDATED, currentAncillaryEquipment.getSerialNumber()));

            // ServiceId must not already be used by another ancillary
            var conflictingAncillary = ancillaryRepository.findByServiceIdAndEquipmentIdNot(updateDTO.getServiceId(), currentAncillaryEquipment.getEquipmentId());
            conflictingAncillary.ifPresent(x -> {
                throw new EqmValidationException(localizedMessageBuilder, ANCILLARY_SERVICE_ALREADY_IN_USE, updateDTO.getServiceId(), x.getSerialNumber());
            });
        }

        process(updateDTO, currentAncillaryEquipment);

        return currentAncillaryEquipment;
    }

    private void process(UpdateAncillaryEquipment updateDTO, AncillaryEquipment currentAncillary) {
        if (Objects.nonNull(updateDTO.getPairedEquipmentId())) {
            Optional<Equipment> pairedEquipment = equipmentRepository.findById(updateDTO.getPairedEquipmentId());
            if (pairedEquipment.isPresent()) {
                var equipment = pairedEquipment.get();
                if (!equipment.getClass().isAssignableFrom(SimCard.class) && !equipment.getClass().isAssignableFrom(CPE.class)) {
                    throw new EqmValidationException(localizedMessageBuilder, ANCILLARY_PAIRED_NOT_VALID);
                }
                currentAncillary.setPairedEquipment(equipment);
            } else {
                throw new EqmValidationException(localizedMessageBuilder, ANCILLARY_EQUIPMENT_PAIRED_EQUIPMENT_NOT_FOUND);
            }
        } else {
            currentAncillary.setPairedEquipment(null);
        }
    }
}

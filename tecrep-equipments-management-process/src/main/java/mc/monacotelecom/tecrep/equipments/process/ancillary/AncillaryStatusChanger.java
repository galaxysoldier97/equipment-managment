package mc.monacotelecom.tecrep.equipments.process.ancillary;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.inventory.common.sm.StatusChanger;
import mc.monacotelecom.tecrep.equipments.dto.AncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.AncillaryChangeStateDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.AncillaryEquipmentDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.entity.CPE;
import mc.monacotelecom.tecrep.equipments.entity.Equipment;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmConflictException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.mapper.AncillaryMapper;
import mc.monacotelecom.tecrep.equipments.repository.AncillaryRepository;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentRepository;
import mc.monacotelecom.tecrep.equipments.state.StateMachineService;
import mc.monacotelecom.tecrep.equipments.utils.AuditCleaner;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static mc.monacotelecom.inventory.common.Lambdas.verifyAndApplyObj;
import static mc.monacotelecom.inventory.common.Lambdas.verifyOrThrow;
import static mc.monacotelecom.tecrep.equipments.enums.Event.activate;
import static mc.monacotelecom.tecrep.equipments.enums.Event.assign;
import static mc.monacotelecom.tecrep.equipments.enums.Status.DEACTIVATED;
import static mc.monacotelecom.tecrep.equipments.enums.Status.REPACKAGING;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AncillaryStatusChanger {
    private final AncillaryRepository ancillaryRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final StateMachineService stateMachineService;
    private final AuditCleaner auditCleaner;
    private final EquipmentRepository<Equipment> equipmentRepository;
    private final Clock clock;
    private final AncillaryMapper ancillaryMapper;

    /**
     * Function that, given an ancillary identifier and action and a set of Optional parameters,
     * changes the status, in case it is present, of the Ancillary according to its cycle.
     *
     * @param id    : target ancillary Id or serialNumber
     * @param dto   : input optional parameters
     * @param event : action to perform
     * @return : updated AncillaryEquipmentDTO
     */
    @Deprecated(since = "2.21.0", forRemoval = true)
    public AncillaryEquipmentDTO applyEventV1(String id, AncillaryChangeStateDTO dto, Event event) {
        return ancillaryMapper.toDtoV1(commonApplyEvent(id, dto, event));
    }

    public AncillaryEquipmentDTOV2 applyEvent(String id, AncillaryChangeStateDTO dto, Event event) {
        return ancillaryMapper.toDtoV2(commonApplyEvent(id, dto, event));
    }

    private AncillaryEquipment commonApplyEvent(String id, AncillaryChangeStateDTO dto, Event event) {
        AncillaryEquipment ancillaryEquipment = null;

        if (StringUtils.isNumeric(id)) {
            ancillaryEquipment = ancillaryRepository.findById(Long.parseLong(id)).orElse(null);
        }

        if (ancillaryEquipment == null) {
            ancillaryEquipment = ancillaryRepository.findBySerialNumber(id)
                    .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, ANCILLARY_EQUIPMENT_NOT_FOUND, id));
        }

        var initialStatus = ancillaryEquipment.getStatus();

        // Initialize recyclable context
        final StatusChanger<Status, Event> statusEventStatusChanger = stateMachineService.getMachine(ancillaryEquipment.getRecyclable());

        try {
            ancillaryEquipment.setStatus(statusEventStatusChanger.updateStatus(String.valueOf(ancillaryEquipment.getEquipmentId()), ancillaryEquipment.getStatus(), event));
        } catch (EqmValidationException e) {
            throw new EqmValidationException(localizedMessageBuilder, ANCILLARY_LIFECYCLE_ERROR, event, ancillaryEquipment.getStatus(), ancillaryEquipment.getSerialNumber());
        }
        verifyOrThrow.test(Objects.isNull(ancillaryEquipment.getIndependent()) || !ancillaryEquipment.getIndependent(),
                new EqmValidationException(localizedMessageBuilder, ANCILLARY_MUST_BE_INDEPENDENT, ancillaryEquipment.getEquipmentId()));

        auditCleaner.cleanWithCondition(ancillaryEquipment.getEquipmentId(), ancillaryRepository, initialStatus, ancillaryEquipment.getStatus());

        changeStateAccordingToEvent(ancillaryEquipment, dto, event);
        log.info(String.format("State changed for Ancillary '%s' from '%s' to '%s' (event '%s')",
                ancillaryEquipment.getSerialNumber(), initialStatus, ancillaryEquipment.getStatus(), event));

        incrementNumberOfRecycles(ancillaryEquipment, initialStatus, ancillaryEquipment.getStatus());
        ancillaryRepository.save(ancillaryEquipment);
        return ancillaryEquipment;
    }

    /**
     * function that update the numberCycles for a given equipment if:
     * status move from DEACTIVATED -> ACTIVATED
     *
     * @param equipment: target entity
     * @param oldStatus  : old status
     * @param newStatus  : new status
     */
    void incrementNumberOfRecycles(AncillaryEquipment equipment,
                                   final Status oldStatus,
                                   final Status newStatus) {
        if ((List.of(DEACTIVATED, REPACKAGING).contains(oldStatus)) && Status.AVAILABLE.equals(newStatus)) {
            final var numberRecycle = equipment.getNumberRecycles();
            equipment.setNumberRecycles(Long.sum(numberRecycle, 1));
        }
    }

    private void changeStateAccordingToEvent(AncillaryEquipment ancillaryEquipment, AncillaryChangeStateDTO changeStateDTO, Event event) {
        switch (event) {
            case available:
                ancillaryEquipment.setAssignmentDate(null);
                ancillaryEquipment.setActivationDate(null);
                break;
            case assign:
            case activate:
                this.updateAttributesForAssignActivate(ancillaryEquipment, event, changeStateDTO);
                break;
            case book:
                if (changeStateDTO.getOrderId() != null) {
                    ancillaryEquipment.setOrderId(changeStateDTO.getOrderId());
                }
                ancillaryEquipment.setServiceId(null);
                break;
            case free:
                ancillaryEquipment.setOrderId(null);
                break;
            case unassign:
                ancillaryEquipment.setAssignmentDate(null);
                ancillaryEquipment.setOrderId(null);
                ancillaryEquipment.setPairedEquipment(null);
                ancillaryEquipment.setServiceId(null);
                break;
            case onhold:
            case deactivate:
                ancillaryEquipment.setOrderId(null);
                ancillaryEquipment.setPairedEquipment(null);
                ancillaryEquipment.setServiceId(null);
                break;
            case rollback_activate:
            case rollback_onhold:
            case rollback_deactivate:
                this.updateAttributesForRollback(ancillaryEquipment, event);
                break;
            case repackage:
                ancillaryEquipment.setExternalNumber(null);
                break;
            default:
                break;
        }
    }

    private void updateAttributesForAssignActivate(AncillaryEquipment ancillaryEquipment, final Event event, final AncillaryChangeStateDTO changeStateDTO) {
        updatePairedEquipmentIdForAssignActivate(ancillaryEquipment, changeStateDTO.getPairedEquipmentId());
        updateServiceIdForAssignActivate(ancillaryEquipment, changeStateDTO.getServiceId());

        verifyAndApplyObj.accept(activate.equals(event), time -> ancillaryEquipment.setActivationDate((LocalDateTime) time), LocalDateTime.now(clock));
        verifyAndApplyObj.accept(assign.equals(event), time -> ancillaryEquipment.setAssignmentDate((LocalDateTime) time), LocalDateTime.now(clock));

        ancillaryEquipment.setOrderId(null);
    }

    private void updateServiceIdForAssignActivate(AncillaryEquipment ancillaryEquipment, Long newServiceId) {
        final Long serviceId = ancillaryEquipment.getServiceId();

        // If service ID is provided but does not match current serviceId
        verifyOrThrow.test(serviceId != null && newServiceId != null && !newServiceId.equals(serviceId),
                new EqmValidationException(localizedMessageBuilder, ANCILLARY_NOT_SAME_SERVICE_ID, newServiceId, serviceId));

        // If independent ancillary and serviceId is provided, we'll try to update it
        if (Boolean.TRUE.equals(ancillaryEquipment.getIndependent()) && newServiceId != null) {
            // ServiceId must not already be used by another ancillary
            var conflictingAncillary = ancillaryRepository.findByServiceIdAndEquipmentIdNot(newServiceId, ancillaryEquipment.getEquipmentId());
            conflictingAncillary.ifPresent(x -> {
                throw new EqmValidationException(localizedMessageBuilder, ANCILLARY_SERVICE_ALREADY_IN_USE, newServiceId, x.getSerialNumber());
            });
            ancillaryEquipment.setServiceId(newServiceId);
        }
    }

    private void updatePairedEquipmentIdForAssignActivate(AncillaryEquipment ancillaryEquipment, Long newPairedEquipmentId) {
        final var oldPairedEquipment = ancillaryEquipment.getPairedEquipment();

        // If no current paired equipment and not independent
        verifyOrThrow.test(oldPairedEquipment == null && Boolean.FALSE.equals(ancillaryEquipment.getIndependent()),
                new EqmValidationException(localizedMessageBuilder, ANCILLARY_PAIRED_REQUIRED));

        // If paired equipment ID is provided, but does not match the current one
        if (oldPairedEquipment != null && newPairedEquipmentId != null && !newPairedEquipmentId.equals(oldPairedEquipment.getEquipmentId())) {
            throw new EqmValidationException(localizedMessageBuilder, ANCILLARY_PAIRED_NOT_SAME_ID, newPairedEquipmentId, oldPairedEquipment.getEquipmentId());
        }

        if (newPairedEquipmentId != null) {
            Optional<Equipment> pairedEquipment = equipmentRepository.findById(newPairedEquipmentId);
            if (pairedEquipment.isPresent()) {
                var equipment = pairedEquipment.get();
                verifyOrThrow.test(!equipment.getClass().isAssignableFrom(SimCard.class) && !equipment.getClass().isAssignableFrom(CPE.class),
                        new EqmValidationException(localizedMessageBuilder, ANCILLARY_PAIRED_NOT_VALID));
                ancillaryEquipment.setPairedEquipment(equipment);
            } else {
                throw new EqmValidationException(localizedMessageBuilder, ANCILLARY_EQUIPMENT_PAIRED_EQUIPMENT_NOT_FOUND, newPairedEquipmentId);
            }
        }
    }

    private void updateAttributesForRollback(AncillaryEquipment ancillaryEquipment, Event event) {
        Revisions<Integer, AncillaryEquipment> revisions = ancillaryRepository.findRevisions(ancillaryEquipment.getEquipmentId());
        List<Revision<Integer, AncillaryEquipment>> data = revisions.getContent();
        List<Revision<Integer, AncillaryEquipment>> fiteredRevisions = new ArrayList<>();
        for (Revision<Integer, AncillaryEquipment> datum : data) {
            if (event.equals(Event.rollback_activate)) {
                if (datum.getEntity().getOrderId() != null) {
                    fiteredRevisions.add(datum);
                }
            } else {
                if (datum.getEntity().getPairedEquipment() != null || datum.getEntity().getServiceId() != null) {
                    fiteredRevisions.add(datum);
                }
            }
        }

        if (!fiteredRevisions.isEmpty()) {
            fiteredRevisions = Lists.reverse(fiteredRevisions);
            var ancillaryEquipmentHistory = fiteredRevisions.get(0).getEntity();
            if (event.equals(Event.rollback_activate)) {
                ancillaryEquipment.setOrderId(ancillaryEquipmentHistory.getOrderId());
            } else {
                if (ancillaryEquipmentHistory.getPairedEquipment() != null) {
                    // Retrieving the equipment clean from the repository seems needed to avoid a "double session" Hibernate issue
                    Equipment oldPairedEquipment = ancillaryEquipmentHistory.getPairedEquipment();
                    var eqt = equipmentRepository.findById(oldPairedEquipment.getEquipmentId())
                            .orElseThrow(() -> new EqmConflictException(localizedMessageBuilder, EQUIPMENT_ROLLBACK_PAIRED_EQUIPMENT_NOT_FOUND, event, ancillaryEquipment.getSerialNumber(), String.valueOf(oldPairedEquipment.getEquipmentId())));
                    ancillaryEquipment.setPairedEquipment(eqt);
                }
                ancillaryEquipment.setServiceId(ancillaryEquipmentHistory.getServiceId());
            }
        }

        if (event.equals(Event.rollback_activate)) {
            ancillaryEquipment.setPairedEquipment(null);
            ancillaryEquipment.setActivationDate(null);
            ancillaryEquipment.setAssignmentDate(null);
            ancillaryEquipment.setServiceId(null);
        }
    }
}

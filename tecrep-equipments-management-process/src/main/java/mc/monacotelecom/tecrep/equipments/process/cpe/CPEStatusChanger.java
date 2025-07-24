package mc.monacotelecom.tecrep.equipments.process.cpe;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.inventory.common.sm.StatusChanger;
import mc.monacotelecom.tecrep.equipments.dto.CPEDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.ChangeStatusDto;
import mc.monacotelecom.tecrep.equipments.dto.v2.CPEDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.CPE;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.mapper.CPEMapper;
import mc.monacotelecom.tecrep.equipments.repository.CPERepository;
import mc.monacotelecom.tecrep.equipments.state.StateMachineService;
import mc.monacotelecom.tecrep.equipments.utils.AuditCleaner;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static mc.monacotelecom.inventory.common.Lambdas.verifyAndApplyObj;
import static mc.monacotelecom.inventory.common.Lambdas.verifyOrThrow;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CPEStatusChanger {
    private final CPERepository cpeRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final StateMachineService stateMachineService;
    private final AuditCleaner auditCleaner;
    private final Clock clock;
    private final CPEMapper cpeMapper;

    /**
     * Function that, given a CPE identifier and action and a set of Optional parameters,
     * changes the status, in case it is present, of the CPE according to it's cycle.
     *
     * @param cpeId           : targer cpeId
     * @param changeStatusDto : input optional parameters
     * @param event           : action to perform
     * @return : CPEDTO with updated status and values
     */
    public CPEDTO applyEventV1(Long cpeId, ChangeStatusDto changeStatusDto, Event event) {
        return cpeMapper.toDto(cpeRepository.save(commonApplyEvent(cpeId, changeStatusDto, event)));
    }

    public CPEDTOV2 applyEvent(Long id, ChangeStatusDto changeStatusDto, Event event) {
        return cpeMapper.toDtoV2(cpeRepository.save(commonApplyEvent(id, changeStatusDto, event)));
    }

    private CPE commonApplyEvent(Long id, ChangeStatusDto changeStatusDto, Event event) {
        var cpe = cpeRepository.findById(id)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, CPE_NOT_FOUND, id));
        var initialStatus = cpe.getStatus();

        StatusChanger<Status, Event> statusEventStatusChanger = stateMachineService.getMachine(cpe.getRecyclable());

        try {
            cpe.setStatus(statusEventStatusChanger.updateStatus(String.valueOf(cpe.getEquipmentId()), cpe.getStatus(), event));
        } catch (EqmValidationException e) {
            throw new EqmValidationException(localizedMessageBuilder, CPE_LIFECYCLE_ERROR, event, cpe.getStatus(), cpe.getSerialNumber());
        }

        auditCleaner.cleanWithCondition(cpe.getEquipmentId(), cpeRepository, initialStatus, cpe.getStatus());
        this.changeStateAccordingToEvent(cpe, changeStatusDto, event);

        log.info(String.format("State changed for CPE '%s' from '%s' to '%s' (event '%s')",
                cpe.getSerialNumber(), initialStatus, cpe.getStatus(), event));

        incrementNumberOfRecycles(cpe, initialStatus, cpe.getStatus());
        return cpe;
    }

    private void changeStateAccordingToEvent(CPE cpe, ChangeStatusDto changeStatusDto, Event event) {
        switch (event) {
            case available:
                cpe.setActivationDate(null);
                cpe.setAssignmentDate(null);
                break;
            case assign:
            case activate:
                this.updateAttributesForAssignActivate(cpe, event, changeStatusDto);
                break;
            case book:
                if (changeStatusDto.getOrderId() != null) {
                    cpe.setOrderId(changeStatusDto.getOrderId());
                }
                cpe.setServiceId(null);
                break;
            case free:
                cpe.setOrderId(null);
                break;
            case unassign:
                cpe.setAssignmentDate(null);
                cpe.setOrderId(changeStatusDto.getOrderId());
                cpe.setServiceId(null);
                break;
            case onhold:
                cpe.setOrderId(changeStatusDto.getOrderId());
                cpe.setServiceId(null);
                break;
            case rollback_activate:
            case rollback_onhold:
                this.updateAttributesForRollback(cpe, event);
                break;
            default:
                break;
        }
    }

    /**
     * function that update the numberCycles for a given equipment if:
     * status move from DEACTIVATED -> ACTIVATED
     *
     * @param equipment: target entity
     * @param oldStatus  : old status
     * @param newStatus  : new status
     */
    void incrementNumberOfRecycles(CPE equipment,
                                   final Status oldStatus,
                                   final Status newStatus) {
        if (Status.DEACTIVATED.equals(oldStatus) && Status.AVAILABLE.equals(newStatus)) {
            final var numberRecycle = equipment.getNumberRecycles();
            equipment.setNumberRecycles(Long.sum(numberRecycle, 1));
        }
    }

    private void updateAttributesForAssignActivate(CPE cpe, final Event event, final ChangeStatusDto changeStatusDto) {
        final Long serviceId = cpe.getServiceId();
        final Long newServiceId = changeStatusDto.getServiceId();

        verifyOrThrow.test(serviceId == null && newServiceId == null,
                new EqmValidationException(localizedMessageBuilder, CPE_REQUIRED_SERVICE_ID));
        verifyOrThrow.test(serviceId != null && !newServiceId.equals(serviceId),
                new EqmValidationException(localizedMessageBuilder, CPE_NOT_SAME_SERVICE_ID, serviceId, newServiceId));

        Optional<CPE> optionalCPE = cpeRepository.findByServiceId(newServiceId);
        optionalCPE.ifPresent(v -> verifyOrThrow.test(!optionalCPE.get().getEquipmentId().equals(cpe.getEquipmentId()),
                new EqmValidationException(localizedMessageBuilder, CPE_ALREADY_LINKED_SERVICE_ID)));

        verifyAndApplyObj.accept(Event.activate.equals(event), time -> cpe.setActivationDate((LocalDateTime) time), LocalDateTime.now(clock));
        verifyAndApplyObj.accept(Event.assign.equals(event), time -> cpe.setAssignmentDate((LocalDateTime) time), LocalDateTime.now(clock));

        cpe.setServiceId(newServiceId);
        cpe.setOrderId(null);
    }

    private void updateAttributesForRollback(CPE cpe, Event event) {
        Revisions<Integer, CPE> revisions = cpeRepository.findRevisions(cpe.getEquipmentId());
        List<Revision<Integer, CPE>> data = revisions.getContent();
        List<Revision<Integer, CPE>> filteredRevisions = new ArrayList<>();
        for (Revision<Integer, CPE> datum : data) {
            if (event.equals(Event.rollback_activate)) {
                if (datum.getEntity().getOrderId() != null) {
                    filteredRevisions.add(datum);
                }
            } else {
                if (datum.getEntity().getServiceId() != null) {
                    filteredRevisions.add(datum);
                }
            }
        }
        if (!filteredRevisions.isEmpty()) {
            filteredRevisions = Lists.reverse(filteredRevisions);
            CPE cpedHistory = filteredRevisions.get(0).getEntity();
            if (event.equals(Event.rollback_activate)) {
                cpe.setOrderId(cpedHistory.getOrderId());
            } else {
                cpe.setServiceId(cpedHistory.getServiceId());
            }
        }
        if (event.equals(Event.rollback_activate)) {
            cpe.setServiceId(null);
            cpe.setActivationDate(null);
            cpe.setAssignmentDate(null);
        }
    }
}

package mc.monacotelecom.tecrep.equipments.process.simcard;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.inventory.common.sm.StatusChanger;
import mc.monacotelecom.tecrep.equipments.dto.SimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.ChangeStatusDto;
import mc.monacotelecom.tecrep.equipments.dto.v2.SimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentCategory;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentNature;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.exceptions.validation.NoServiceIdException;
import mc.monacotelecom.tecrep.equipments.mapper.SimCardMapper;
import mc.monacotelecom.tecrep.equipments.operator.OperatorService;
import mc.monacotelecom.tecrep.equipments.repository.SimCardRepository;
import mc.monacotelecom.tecrep.equipments.state.StateMachineService;
import mc.monacotelecom.tecrep.equipments.utils.AuditCleaner;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static mc.monacotelecom.inventory.common.Lambdas.verifyAndApplyObj;
import static mc.monacotelecom.inventory.common.Lambdas.verifyOrThrow;
import static mc.monacotelecom.tecrep.equipments.enums.Event.rollback_activate;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SimCardStatusChanger {
    private final SimCardRepository simCardRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final StateMachineService stateMachineService;
    private final AuditCleaner auditCleaner;
    private final OperatorService operatorService;
    private final Clock clock;
    private final SimCardMapper simcardMapper;

    /**
     * Function that, given a simcard identifier and action and a set of Optional parameters,
     * changes the status, in case it is present, of the SimCard according to it's cycle.
     *
     * @param simCardId       : id of the target SimCard
     * @param changeStatusDto : set of optional input Parameters
     * @param event           : action to perform
     * @return : SimCardDTO with updated status and values.
     */
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO changeStateByIdV1(Long simCardId, ChangeStatusDto changeStatusDto, Event event) {
        SimCard simCard = simCardRepository.findById(simCardId)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, SIMCARD_NOT_FOUND_ID, String.valueOf(simCardId)));
        return simcardMapper.toDtoV1(applyEvent(simCard, changeStatusDto, event));
    }

    public SimCardDTOV2 changeStateById(Long simCardId, ChangeStatusDto changeStatusDto, Event event) {
        SimCard simCard = simCardRepository.findById(simCardId)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, SIMCARD_NOT_FOUND_ID, String.valueOf(simCardId)));
        return simcardMapper.toDtoV2(applyEvent(simCard, changeStatusDto, event));
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO changeStateByICCIDV1(String serialNumber, ChangeStatusDto changeStatusDto, Event event) {
        SimCard simCard = simCardRepository.findBySerialNumberAndCategory(serialNumber, EquipmentCategory.SIMCARD)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, SIMCARD_NOT_FOUND_ICCID, serialNumber));
        return simcardMapper.toDtoV1(applyEvent(simCard, changeStatusDto, event));
    }

    public SimCardDTOV2 changeStateByICCID(String serialNumber, ChangeStatusDto changeStatusDto, Event event) {
        SimCard simCard = simCardRepository.findBySerialNumberAndCategory(serialNumber, EquipmentCategory.SIMCARD)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, SIMCARD_NOT_FOUND_ICCID, serialNumber));
        return simcardMapper.toDtoV2(applyEvent(simCard, changeStatusDto, event));
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO changeStateByIMSIV1(String imsi, ChangeStatusDto changeStatusDto, Event event) {
        SimCard simCard = simCardRepository.findByImsiNumber(imsi)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, SIMCARD_NOT_FOUND_IMSI, imsi));
        return simcardMapper.toDtoV1(applyEvent(simCard, changeStatusDto, event));
    }

    public SimCardDTOV2 changeStateByIMSI(String imsi, ChangeStatusDto changeStatusDto, Event event) {
        SimCard simCard = simCardRepository.findByImsiNumber(imsi)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, SIMCARD_NOT_FOUND_IMSI, imsi));
        return simcardMapper.toDtoV2(applyEvent(simCard, changeStatusDto, event));
    }

    private SimCard applyEvent(SimCard simCard, ChangeStatusDto changeStatusDto, Event event) {
        var initialStatus = simCard.getStatus();

        final StatusChanger<Status, Event> statusEventStatusChanger = stateMachineService.getMachine(simCard.getRecyclable());

        try {
            simCard.setStatus(statusEventStatusChanger.updateStatus(String.valueOf(simCard.getEquipmentId()), simCard.getStatus(), event));
        } catch (EqmValidationException e) {
            throw new EqmValidationException(localizedMessageBuilder, SIMCARD_LIFECYCLE_ERROR, event, simCard.getStatus(), simCard.getSerialNumber());
        }

        auditCleaner.cleanWithCondition(simCard.getEquipmentId(), simCardRepository, initialStatus, simCard.getStatus());

        changeStateAccordingToEvent(simCard, event, changeStatusDto);
        log.info(String.format("State changed for SIM '%s' from '%s' to '%s' (event '%s')",
                simCard.getSerialNumber(), initialStatus, simCard.getStatus(), event));

        return simCardRepository.save(simCard);
    }

    public void changeStateAccordingToEvent(SimCard simcard, Event event, ChangeStatusDto changeStatusDto) throws EqmValidationException {
        switch (event) {
            case available:
                simcard.setActivationDate(null);
                simcard.setAssignmentDate(null);
                break;
            case assign:
            case activate:
                this.updateAttributesForAssignActivate(simcard, event, changeStatusDto);
                break;
            case book:
                if (changeStatusDto.getOrderId() != null) {
                    simcard.setOrderId(changeStatusDto.getOrderId());
                }
                simcard.setServiceId(null);
                break;
            case free:
                simcard.setOrderId(null);
                break;
            case unassign:
                simcard.setAssignmentDate(null);
                simcard.setOrderId(changeStatusDto.getOrderId());
                simcard.setServiceId(null);
                break;
            case deactivate:
                simcard.setOrderId(changeStatusDto.getOrderId());
                simcard.setServiceId(null);
                break;
            case rollback_activate:
            case rollback_deactivate:
                this.updateAttributesForRollback(simcard, event);
                break;
            default:
                break;
        }
    }

    protected void updateAttributesForAssignActivate(final SimCard simCard, final Event event, final ChangeStatusDto changeStatusDto) {
        final Long serviceId = simCard.getServiceId();
        final Long newServiceId = changeStatusDto.getServiceId();

        final boolean serviceIdsNull = serviceId == null && newServiceId == null && !operatorService.isEir();
        final boolean serviceIdsEquals = serviceId != null && !serviceId.equals(newServiceId);

        verifyOrThrow.test(serviceIdsNull, new NoServiceIdException(localizedMessageBuilder, SIMCARD_REQUIRED_SERVICE_ID));
        verifyOrThrow.test(serviceIdsEquals, new EqmValidationException(localizedMessageBuilder, SIMCARD_NOT_SAME_PROVIDER_SERVICE_ID, newServiceId, serviceId));

        if (!operatorService.isEir() && EquipmentNature.MAIN.equals(simCard.getNature())) {
            // [MTOSS-1376] Multiple SIMs having the same serviceId is OK if there is only one MAIN among them
            SimCardUpdater.validateServiceCanBeSetOnNewMainSim(newServiceId, simCard.getEquipmentId(), simCardRepository, localizedMessageBuilder);
        }

        verifyAndApplyObj.accept(Event.activate.equals(event), time -> simCard.setActivationDate((LocalDateTime) time), LocalDateTime.now(clock));
        verifyAndApplyObj.accept(Event.assign.equals(event), time -> simCard.setAssignmentDate((LocalDateTime) time), LocalDateTime.now(clock));

        simCard.setServiceId(newServiceId);
        simCard.setOrderId(null);
    }

    protected void updateAttributesForRollback(final SimCard simCard, final Event event) {
        Revisions<Integer, SimCard> revisions = simCardRepository.findRevisions(simCard.getEquipmentId());
        List<Revision<Integer, SimCard>> data = revisions.getContent();
        List<Revision<Integer, SimCard>> filteredRevisions = new ArrayList<>();
        for (Revision<Integer, SimCard> datum : data) {
            if (event.equals(rollback_activate)) {
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
            SimCard simCardHistory = filteredRevisions.get(0).getEntity();
            if (event.equals(rollback_activate)) {
                simCard.setOrderId(simCardHistory.getOrderId());
            } else {
                if (EquipmentNature.MAIN.equals(simCard.getNature())) {
                    SimCardUpdater.validateServiceCanBeSetOnNewMainSim(simCardHistory.getServiceId(), simCard.getEquipmentId(), simCardRepository, localizedMessageBuilder);
                }
                simCard.setServiceId(simCardHistory.getServiceId());
            }
        }
        if (event.equals(rollback_activate)) {
            simCard.setServiceId(null);
            simCard.setActivationDate(null);
            simCard.setAssignmentDate(null);
        }
    }
}


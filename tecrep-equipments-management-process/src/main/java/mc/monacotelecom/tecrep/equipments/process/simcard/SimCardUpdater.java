package mc.monacotelecom.tecrep.equipments.process.simcard;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.inventory.common.Lambdas;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.dto.SimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.UpdateSimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateSimCard;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateSimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.SimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.entity.Warehouse;
import mc.monacotelecom.tecrep.equipments.enums.*;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmConflictException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.mapper.SimCardMapper;
import mc.monacotelecom.tecrep.equipments.operator.OperatorService;
import mc.monacotelecom.tecrep.equipments.repository.SimCardRepository;
import mc.monacotelecom.tecrep.equipments.repository.WarehouseRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static mc.monacotelecom.inventory.common.Lambdas.verifyAndApplyObj;
import static mc.monacotelecom.inventory.common.Lambdas.verifyAndApplyString;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.*;

@Component
@RequiredArgsConstructor
public class SimCardUpdater {
    private final SimCardRepository simCardRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final WarehouseRepository warehouseRepository;
    private final OperatorService operatorService;
    private final SimCardMapper simCardMapper;

    /**
     * Update sim card equipment only with information filled (not null and not empty) in input DTO
     *
     * @param id  : the sim card equipment id, must reference an existing simcard
     * @param dto : sim card input parameters
     * @return : updated sim card
     */
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO partialUpdateByIdV1(final Long id, UpdateSimCardDTO dto) {
        SimCard simCard = validateV1(simCardRepository.findById(id), SIMCARD_NOT_FOUND_ID, String.valueOf(id), dto);
        return simCardMapper.toDtoV1(partialUpdate(simCard, dto));
    }

    public SimCardDTOV2 partialUpdateById(Long id, UpdateSimCardDTOV2 dto) {
        SimCard simCard = validate(simCardRepository.findById(id), SIMCARD_NOT_FOUND_ID, String.valueOf(id), dto);
        return simCardMapper.toDtoV2(partialUpdateV2(simCard, dto));
    }

    /**
     * Update sim card equipment only with information filled (not null and not empty) in input DTO
     *
     * @param iccid : the sim card equipment iccid, must reference an existing simcard
     * @param dto   : sim card input parameters
     * @return : updated sim card
     */
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO partialUpdateByIccidV1(final String iccid, UpdateSimCardDTO dto) {
        SimCard simCard = validateV1(simCardRepository.findBySerialNumberAndCategory(iccid, EquipmentCategory.SIMCARD), SIMCARD_NOT_FOUND_ICCID, String.valueOf(iccid), dto);
        return simCardMapper.toDtoV1(partialUpdate(simCard, dto));
    }

    public SimCardDTOV2 partialUpdateByIccid(String iccid, UpdateSimCardDTOV2 dto) {
        SimCard simCard = validate(simCardRepository.findBySerialNumberAndCategory(iccid, EquipmentCategory.SIMCARD), SIMCARD_NOT_FOUND_ICCID, String.valueOf(iccid), dto);
        return simCardMapper.toDtoV2(partialUpdateV2(simCard, dto));
    }

    /**
     * Update sim card equipment only with information filled (not null and not empty) in input DTO
     *
     * @param imsi : the sim card equipment imsi, must reference an existing simcard
     * @param dto  : sim card input parameters
     * @return : updated sim card
     */
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO partialUpdateByImsiV1(final String imsi, UpdateSimCardDTO dto) {
        SimCard simCard = validateV1(simCardRepository.findByImsiNumber(imsi), SIMCARD_NOT_FOUND_IMSI, String.valueOf(imsi), dto);
        return simCardMapper.toDtoV1(partialUpdate(simCard, dto));
    }

    public SimCardDTOV2 partialUpdateByImsi(String imsi, UpdateSimCardDTOV2 dto) {
        SimCard simCard = validate(simCardRepository.findByImsiNumber(imsi), SIMCARD_NOT_FOUND_IMSI, String.valueOf(imsi), dto);
        return simCardMapper.toDtoV2(partialUpdateV2(simCard, dto));
    }

    private SimCard validate(Optional<SimCard> simCardRepository, String simcardNotFoundImsi, String imsi, UpdateSimCardDTOV2 dto) {
        SimCard simCard = simCardRepository
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, simcardNotFoundImsi, imsi));

        commonValidate(dto, simCard);

        if (StringUtils.isNotBlank(dto.getWarehouseName())) {
            simCard.setWarehouse(warehouseRepository.findByName(dto.getWarehouseName())
                    .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, WAREHOUSE_NAME_NOT_FOUND, dto.getWarehouseName())));
        }
        return simCard;
    }

    private SimCard validateV1(Optional<SimCard> simCardRepository, String simcardNotFoundImsi, String imsi, UpdateSimCardDTO dto) {
        SimCard simCard = simCardRepository
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, simcardNotFoundImsi, imsi));

        commonValidate(dto, simCard);

        if (dto.getWarehouseId() != null) {
            Warehouse warehouse = warehouseRepository.findById(dto.getWarehouseId())
                    .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, WAREHOUSE_ID_NOT_FOUND, dto.getWarehouseId()));
            simCard.setWarehouse(warehouse);
        }
        return simCard;
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO updateV1(final Long id, UpdateSimCardDTO dto) {
        SimCard simCard = simCardRepository.save(commonUpdate(id, dto));

        final Warehouse warehouse = Objects.nonNull(dto.getWarehouseId()) ? warehouseRepository.findById(dto.getWarehouseId()).orElse(null) : null;
        simCard.setWarehouse(warehouse);

        return simCardMapper.toDtoV1(simCard);
    }

    public SimCardDTOV2 update(Long id, UpdateSimCardDTOV2 dto) {
        SimCard simCard = commonUpdate(id, dto);

        verifyAndApplyString.accept(StringUtils.isNotBlank(dto.getActivationCode()), simCard::setActivationCode, dto.getActivationCode());
        verifyAndApplyObj.accept(Objects.nonNull(dto.getConfirmationCode()), code -> simCard.setConfirmationCode((Integer) code), dto.getConfirmationCode());
        verifyAndApplyObj.accept(StringUtils.isNotBlank(dto.getQrCode()), qrCode -> simCard.setQrCode((String) qrCode), dto.getQrCode());

        simCard.setWarehouse(warehouseRepository.findByName(dto.getWarehouseName())
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, WAREHOUSE_NAME_NOT_FOUND, dto.getWarehouseName())));

        return simCardMapper.toDtoV2(simCardRepository.save(simCard));
    }

    /**
     * Update SimCard equipment with all information in input DTO, even if not filled or null
     *
     * @param simCardId : the sim card equipment id, must reference an existing simcard
     * @param dto       : sim card input parameters
     * @return : updated sim card
     */
    public SimCard commonUpdate(final Long simCardId, UpdateSimCard dto) {
        SimCard actualSimCard = simCardRepository.findById(simCardId)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, SIMCARD_NOT_FOUND_ID, String.valueOf(simCardId)));

        commonValidate(dto, actualSimCard);

        actualSimCard.setImsiNumber(dto.getImsiNumber());
        actualSimCard.setImsiSponsorNumber(dto.getImsiSponsorNumber());
        actualSimCard.setPin1Code(dto.getPin1Code());
        actualSimCard.setPin2Code(dto.getPin2Code());
        actualSimCard.setPuk1Code(dto.getPuk1Code());
        actualSimCard.setPuk2Code(dto.getPuk2Code());
        actualSimCard.setAuthKey(dto.getAuthKey());
        actualSimCard.setAccessControlClass(dto.getAccessControlClass());
        actualSimCard.setPackId(dto.getPackId());
        actualSimCard.setOrderId(dto.getOrderId());
        actualSimCard.setSimProfile(dto.getSimProfile());
        actualSimCard.setNumber(dto.getNumber());
        actualSimCard.setBatchNumber(dto.getBatchNumber());
        actualSimCard.setExternalNumber(dto.getExternalNumber());
        actualSimCard.setActivity(dto.getActivity());
        actualSimCard.setNature(dto.getNature());
        actualSimCard.setBrand(dto.getBrand());

        verifyAndApplyObj.accept(Objects.nonNull(dto.getAccessType()),
                accessType -> actualSimCard.setAccessType((AccessType) accessType), dto.getAccessType());

        return actualSimCard;
    }

    public void commonValidate(UpdateSimCard dto, SimCard simToUpdate) {
        this.validateAttributes(dto, simToUpdate);

        validateNatureAndServiceId(dto, simToUpdate);

        if (dto.getServiceId() != null) {
            final boolean isNotBookedAssignedActivated = !List.of(Status.BOOKED, Status.ASSIGNED, Status.ACTIVATED).contains(simToUpdate.getStatus());
            Lambdas.verifyOrThrow.test(isNotBookedAssignedActivated,
                    new EqmValidationException(localizedMessageBuilder, WRONG_EQUIPMENT_STATUS, simToUpdate.getStatus()));

            final boolean isNotActivatedAssigned = !List.of(Status.ACTIVATED, Status.ASSIGNED).contains(simToUpdate.getStatus());
            Lambdas.verifyOrThrow.test(isNotActivatedAssigned && !Objects.equals(dto.getServiceId(), simToUpdate.getServiceId()),
                    new EqmValidationException(localizedMessageBuilder, SIMCARD_SERVICE_STATUS_UPDATE_ASSIGNED_OR_ACTIVATED_VALIDATION));
            simToUpdate.setServiceId(dto.getServiceId());
        }

        final boolean isServiceIdNullAndStatusActivatedOrAssigned = dto.getServiceId() == null && simToUpdate.getServiceId() == null
                && (Status.ACTIVATED.equals(simToUpdate.getStatus()) || Status.ASSIGNED.equals(simToUpdate.getStatus()));
        final boolean isImsiNotNullAndInvalidLength = dto.getImsiNumber() != null && dto.getImsiNumber().length() != 15;
        final boolean isOrderAndNotBooked = StringUtils.isNoneBlank(dto.getOrderId()) && !simToUpdate.getStatus().equals(Status.BOOKED);

        Lambdas.verifyOrThrow.test(isServiceIdNullAndStatusActivatedOrAssigned && !operatorService.isEir(),
                new EqmValidationException(localizedMessageBuilder, SIMCARD_SERVICE_NOT_NULL_STATUS_ASSIGNED_OR_ACTIVATED));
        Lambdas.verifyOrThrow.test(isImsiNotNullAndInvalidLength,
                new EqmValidationException(localizedMessageBuilder, SIMCARD_IMSI_LENGTH_VALIDATION));
        Lambdas.verifyOrThrow.test(isOrderAndNotBooked,
                new EqmValidationException(localizedMessageBuilder, SIMCARD_ORDERID_CHECK_STATUS_BOOKED));
    }

    /**
     * Helper function to validate that Nature and ServiceId are consistent during an update operation, partial or full
     */
    private void validateNatureAndServiceId(UpdateSimCard dto, SimCard simToUpdate) {
        EquipmentNature nature = dto.getNature() != null ? dto.getNature() : simToUpdate.getNature();
        Long serviceId = dto.getServiceId() != null ? dto.getServiceId() : simToUpdate.getServiceId();

        if (EquipmentNature.MAIN.equals(nature) && serviceId != null) {
            validateServiceCanBeSetOnNewMainSim(serviceId, simToUpdate.getEquipmentId(), simCardRepository, localizedMessageBuilder);
        }
    }

    /**
     * A given ServiceId can only be given to a single MAIN SIM, in addition to optional ADDITIONAL ones
     */
    public static void validateServiceCanBeSetOnNewMainSim(Long serviceId, Long newSimId, SimCardRepository simCardRepository, LocalizedMessageBuilder localizedMessageBuilder) {
        simCardRepository.findByServiceId(serviceId).stream()
                .filter(x -> EquipmentNature.MAIN.equals(x.getNature()) && !x.getEquipmentId().equals(newSimId)).findFirst()
                .ifPresent(x -> {
                    throw new EqmValidationException(localizedMessageBuilder, SIMCARD_ALREADY_MAIN_FOR_SERVICE, String.valueOf(serviceId), x.getSerialNumber());
                });
    }

    @SuppressWarnings("java:S3776")
    public void validateAttributes(UpdateSimCard dto, SimCard simToUpdate) {
        if (Objects.nonNull(dto.getImsiNumber())) {
            simCardRepository.findByImsiNumber(dto.getImsiNumber()).ifPresent(existingSim -> {
                if (!existingSim.getEquipmentId().equals(simToUpdate.getEquipmentId())) {
                    throw new EqmConflictException(localizedMessageBuilder, SIMCARD_IMSI_ALREADY_IN_USE, dto.getImsiNumber());
                }
            });
        }

        if (Objects.nonNull(dto.getImsiSponsorNumber())) {
            simCardRepository.findByImsiSponsorNumber(dto.getImsiSponsorNumber()).ifPresent(existingSim -> {
                if (!existingSim.getEquipmentId().equals(simToUpdate.getEquipmentId())) {
                    throw new EqmConflictException(localizedMessageBuilder, SIMCARD_IMSI_SPONSOR_NUMBER_ALREADY_IN_USE, dto.getImsiSponsorNumber());
                }
            });
        }

        if (Objects.nonNull(dto.getSerialNumber())) {
            simCardRepository.findBySerialNumberAndCategory(dto.getSerialNumber(), EquipmentCategory.SIMCARD).ifPresent(existingSim -> {
                if (!existingSim.getEquipmentId().equals(simToUpdate.getEquipmentId())) {
                    throw new EqmConflictException(localizedMessageBuilder, SIMCARD_SERIAL_NUMBER_ALREADY_IN_USE, dto.getSerialNumber(), simToUpdate.getSerialNumber());
                }
            });
        }

        if (Objects.nonNull(dto.getPackId())) {
            simCardRepository.findByPackId(dto.getPackId()).ifPresent(existingSim -> {
                if (!existingSim.getEquipmentId().equals(simToUpdate.getEquipmentId())) {
                    throw new EqmConflictException(localizedMessageBuilder, SIMCARD_PACK_ID_ALREADY_IN_USE);
                }
            });
        }
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO updatePackByIdV1(Long id, UpdateSimCardDTO dto) {
        return simCardMapper.toDtoV1(simCardRepository.save(validateUpdatePack(id, dto)));
    }

    public SimCardDTOV2 updatePackById(Long id, UpdateSimCardDTOV2 dto) {
        return simCardMapper.toDtoV2(simCardRepository.save(validateUpdatePack(id, dto)));
    }

    private SimCard validateUpdatePack(Long id, UpdateSimCard dto) {
        SimCard simCard = simCardRepository.findById(id)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, SIMCARD_NOT_FOUND_ID, String.valueOf(id)));

        Lambdas.verifyOrThrow.test(Objects.isNull(dto.getPackId()),
                new EqmValidationException(localizedMessageBuilder, SIMCARD_NOT_VALID_PACK_ID));
        simCard.setPackId(dto.getPackId());
        return simCard;
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO updatePackByImsiV1(UpdateSimCardDTO dto) {
        return simCardMapper.toDtoV1(simCardRepository.save(validateUpdatePackIMSI(dto)));
    }

    public SimCardDTOV2 updatePackByImsi(UpdateSimCardDTOV2 dto) {
        return simCardMapper.toDtoV2(simCardRepository.save(validateUpdatePackIMSI(dto)));
    }

    private SimCard validateUpdatePackIMSI(UpdateSimCard dto) {
        Lambdas.verifyOrThrow.test(Objects.isNull(dto.getImsiNumber()),
                new EqmValidationException(localizedMessageBuilder, SIMCARD_IMSI_REQUIRED));
        Lambdas.verifyOrThrow.test(dto.getImsiNumber().length() != 15,
                new EqmValidationException(localizedMessageBuilder, SIMCARD_IMSI_LENGTH_VALIDATION));
        Lambdas.verifyOrThrow.test(Objects.isNull(dto.getPackId()),
                new EqmValidationException(localizedMessageBuilder, SIMCARD_NOT_VALID_PACK_ID));

        SimCard simCard = simCardRepository.findByImsiNumber(dto.getImsiNumber())
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, SIMCARD_NOT_FOUND_IMSI, dto.getImsiNumber()));
        simCard.setPackId(dto.getPackId());
        return simCard;
    }

    private SimCard partialUpdate(final SimCard simCard, final UpdateSimCard updateSimCardDTO) {
        verifyAndApplyString.accept(StringUtils.isNotBlank(updateSimCardDTO.getImsiNumber()), simCard::setImsiNumber, updateSimCardDTO.getImsiNumber());
        verifyAndApplyString.accept(StringUtils.isNotBlank(updateSimCardDTO.getPin1Code()), simCard::setPin1Code, updateSimCardDTO.getPin1Code());
        verifyAndApplyString.accept(StringUtils.isNotBlank(updateSimCardDTO.getPin2Code()), simCard::setPin2Code, updateSimCardDTO.getPin2Code());
        verifyAndApplyString.accept(StringUtils.isNotBlank(updateSimCardDTO.getPuk1Code()), simCard::setPuk1Code, updateSimCardDTO.getPuk1Code());
        verifyAndApplyString.accept(StringUtils.isNotBlank(updateSimCardDTO.getPuk2Code()), simCard::setPuk2Code, updateSimCardDTO.getPuk2Code());
        verifyAndApplyString.accept(StringUtils.isNotBlank(updateSimCardDTO.getAuthKey()), simCard::setAuthKey, updateSimCardDTO.getAuthKey());
        verifyAndApplyString.accept(StringUtils.isNotBlank(updateSimCardDTO.getBrand()), simCard::setBrand, updateSimCardDTO.getBrand());
        verifyAndApplyString.accept(StringUtils.isNotBlank(updateSimCardDTO.getAccessControlClass()), simCard::setAccessControlClass, updateSimCardDTO.getAccessControlClass());
        verifyAndApplyString.accept(StringUtils.isNotBlank(updateSimCardDTO.getPackId()), simCard::setPackId, updateSimCardDTO.getPackId());
        verifyAndApplyString.accept(StringUtils.isNotBlank(updateSimCardDTO.getOrderId()), simCard::setOrderId, updateSimCardDTO.getOrderId());
        verifyAndApplyString.accept(StringUtils.isNotBlank(updateSimCardDTO.getNumber()), simCard::setNumber, updateSimCardDTO.getNumber());
        verifyAndApplyString.accept(StringUtils.isNoneBlank(updateSimCardDTO.getBatchNumber()), simCard::setBatchNumber, updateSimCardDTO.getBatchNumber());
        verifyAndApplyString.accept(StringUtils.isNotBlank(updateSimCardDTO.getExternalNumber()), simCard::setExternalNumber, updateSimCardDTO.getExternalNumber());
        verifyAndApplyObj.accept(Objects.nonNull(updateSimCardDTO.getSimProfile()), profile -> simCard.setSimProfile((String) profile), updateSimCardDTO.getSimProfile());
        verifyAndApplyObj.accept(Objects.nonNull(updateSimCardDTO.getAccessType()), accessType -> simCard.setAccessType((AccessType) accessType), updateSimCardDTO.getAccessType());
        verifyAndApplyObj.accept(Objects.nonNull(updateSimCardDTO.getActivity()), activity -> simCard.setActivity((Activity) activity), updateSimCardDTO.getActivity());
        verifyAndApplyObj.accept(Objects.nonNull(updateSimCardDTO.getNature()), nature -> simCard.setNature((EquipmentNature) nature), updateSimCardDTO.getNature());
        verifyAndApplyObj.accept(Objects.nonNull(updateSimCardDTO.getEsim()), esim -> simCard.setEsim((boolean) esim), updateSimCardDTO.getEsim());

        return simCardRepository.save(simCard);
    }

    public SimCard partialUpdateV2(final SimCard simCard, final UpdateSimCardDTOV2 updateSimCardDTO) {
        verifyAndApplyString.accept(StringUtils.isNotBlank(updateSimCardDTO.getActivationCode()), simCard::setActivationCode, updateSimCardDTO.getActivationCode());
        verifyAndApplyObj.accept(Objects.nonNull(updateSimCardDTO.getConfirmationCode()), code -> simCard.setConfirmationCode((Integer) code), updateSimCardDTO.getConfirmationCode());
        verifyAndApplyObj.accept(StringUtils.isNotBlank(updateSimCardDTO.getQrCode()), qRCode -> simCard.setQrCode((String) qRCode), updateSimCardDTO.getQrCode());

        return partialUpdate(simCard, updateSimCardDTO);
    }
}

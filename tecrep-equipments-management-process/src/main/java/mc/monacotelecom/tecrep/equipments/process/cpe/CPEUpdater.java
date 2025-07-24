package mc.monacotelecom.tecrep.equipments.process.cpe;

import mc.monacotelecom.inventory.common.Lambdas;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.assembler.CPEResourceAssembler;
import mc.monacotelecom.tecrep.equipments.dto.CPEDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.UpdateCPEDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateCPE;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateCPEDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.CPEDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.CPE;
import mc.monacotelecom.tecrep.equipments.entity.Warehouse;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.importer.validators.CPEValidators;
import mc.monacotelecom.tecrep.equipments.mapper.CPEMapper;
import mc.monacotelecom.tecrep.equipments.repository.CPERepository;
import mc.monacotelecom.tecrep.equipments.repository.WarehouseRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.*;

@Component
public class CPEUpdater {
    private final CPERepository cpeRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final CPEResourceAssembler cpeResourceAssembler;
    private final WarehouseRepository warehouseRepository;
    private final CPEValidators cpeValidators;
    private final CPEMapper cpeMapper;

    public CPEUpdater(final CPERepository cpeRepository,
                      final LocalizedMessageBuilder localizedMessageBuilder,
                      final WarehouseRepository warehouseRepository,
                      final CPEValidators cpeValidators,
                      final CPEMapper cpeMapper) {
        this.cpeRepository = cpeRepository;
        this.localizedMessageBuilder = localizedMessageBuilder;
        this.warehouseRepository = warehouseRepository;
        this.cpeValidators = cpeValidators;
        this.cpeMapper = cpeMapper;
        this.cpeResourceAssembler = CPEResourceAssembler.of(CPECreator.class, cpeMapper);
    }

    /**
     * Update CPE with all information in input DTO, even if filled or not filled
     *
     * @param id  : CPE target id
     * @param dto : input parameters
     * @return : updated CPE
     */
    @Deprecated(since = "2.21.0", forRemoval = true)
    public CPEDTO updateV1(final Long id, UpdateCPEDTO dto) {
        CPE cpe = commonUpdate(id, dto);

        final boolean isWareHousePresent = Objects.nonNull(dto.getWarehouse()) && Objects.nonNull(dto.getWarehouse().getWarehouseId());
        final Warehouse warehouse = isWareHousePresent ? warehouseRepository.findById(dto.getWarehouse().getWarehouseId()).orElse(null) : null;
        cpe.setWarehouse(warehouse);

        return cpeResourceAssembler.toModel(cpe);
    }

    public CPEDTOV2 update(final Long id, UpdateCPEDTOV2 dto) {
        CPE cpe = commonUpdate(id, dto);

        cpe.setWarehouse(warehouseRepository.findByName(dto.getWarehouseName())
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, WAREHOUSE_NAME_NOT_FOUND, dto.getWarehouseName())));

        return cpeMapper.toDtoV2(cpe);
    }

    private CPE commonUpdate(Long id, UpdateCPE dto) {
        var cpe = cpeCheckBeforeProcess(id, dto, false);
        cpe.setMacAddressCpe(dto.getMacAddressCpe());
        cpe.setMacAddressRouter(dto.getMacAddressRouter());
        cpe.setMacAddressVoip(dto.getMacAddressVoip());
        cpe.setMacAddress5G(dto.getMacAddress5G());
        cpe.setMacAddressLan(dto.getMacAddressLan());
        cpe.setMacAddress4G(dto.getMacAddress4G());
        cpe.setChipsetId(dto.getChipsetId());
        cpe.setHwVersion(dto.getHwVersion());
        cpe.setOrderId(dto.getOrderId());
        cpe.setBatchNumber(dto.getBatchNumber());
        cpe.setExternalNumber(dto.getExternalNumber());
        return cpeRepository.save(cpe);
    }

    /**
     * Update CPE with only information filled in input DTO
     *
     * @param id  : CPE target id
     * @param dto : input parameters
     * @return : updated CPE
     */
    @Deprecated(since = "2.21.0", forRemoval = true)
    public CPEDTO partialUpdateV1(final Long id, UpdateCPEDTO dto) {
        CPE cpe = commonPartialUpdate(id, dto);

        if (Objects.nonNull(dto.getWarehouse()) && Objects.nonNull(dto.getWarehouse().getWarehouseId())) {
            final var warehouse = warehouseRepository.findById(dto.getWarehouse().getWarehouseId()).orElseThrow(() ->
                    new EqmValidationException(localizedMessageBuilder, WAREHOUSE_ID_NOT_FOUND, dto.getWarehouse().getWarehouseId()));
            cpe.setWarehouse(warehouse);
        }

        return cpeResourceAssembler.toModel(cpe);
    }

    public CPEDTOV2 partialUpdate(Long id, UpdateCPEDTOV2 dto) {
        CPE cpe = commonPartialUpdate(id, dto);

        if (StringUtils.isNotBlank(dto.getWarehouseName())) {
            cpe.setWarehouse(warehouseRepository.findByName(dto.getWarehouseName())
                    .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, WAREHOUSE_NAME_NOT_FOUND, dto.getWarehouseName())));
        }

        return cpeMapper.toDtoV2(cpe);
    }

    private CPE commonPartialUpdate(Long id, UpdateCPE dto) {
        var cpe = cpeCheckBeforeProcess(id, dto, true);

        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(dto.getMacAddressCpe()), cpe::setMacAddressCpe, dto.getMacAddressCpe());
        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(dto.getMacAddressRouter()), cpe::setMacAddressRouter, dto.getMacAddressRouter());
        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(dto.getMacAddressVoip()), cpe::setMacAddressVoip, dto.getMacAddressVoip());
        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(dto.getMacAddress5G()), cpe::setMacAddress5G, dto.getMacAddress5G());
        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(dto.getMacAddressLan()), cpe::setMacAddressLan, dto.getMacAddressLan());
        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(dto.getMacAddress4G()), cpe::setMacAddress4G, dto.getMacAddress4G());
        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(dto.getChipsetId()), cpe::setChipsetId, dto.getChipsetId());
        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(dto.getHwVersion()), cpe::setHwVersion, dto.getHwVersion());
        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(dto.getOrderId()), cpe::setOrderId, dto.getOrderId());
        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(dto.getBatchNumber()), cpe::setBatchNumber, dto.getBatchNumber());
        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(dto.getExternalNumber()), cpe::setExternalNumber, dto.getExternalNumber());

        return cpeRepository.save(cpe);
    }

    private CPE cpeCheckBeforeProcess(final Long id, UpdateCPE dto, boolean isPatch) {
        var cpe = cpeRepository.findByEquipmentId(id)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, CPE_NOT_FOUND, id));

        final boolean notBookAssignedActivatedOnHold = !List.of(Status.BOOKED, Status.ASSIGNED, Status.ONHOLD, Status.ACTIVATED).contains(cpe.getStatus());
        final boolean notActivatedAssigned = !List.of(Status.ACTIVATED, Status.ASSIGNED).contains(cpe.getStatus());

        if (isPatch) {
            cpeValidators.validateCpeAttributeExistenceBeforePartialUpdate(dto, cpe);
        } else {
            cpeValidators.validateCpeAttributeExistenceBeforeUpdate(dto, cpe);
        }

        if (dto.getServiceId() != null) {
            Optional<CPE> optionalCPE = cpeRepository.findByServiceId(dto.getServiceId());
            Long finalServiceId = dto.getServiceId();
            optionalCPE.ifPresentOrElse(v -> Lambdas.verifyOrThrow.test(!v.getEquipmentId().equals(cpe.getEquipmentId()),
                            new EqmValidationException(localizedMessageBuilder, CPE_SERVICE_ALREADY_IN_USE)),
                    () -> {
                        Lambdas.verifyOrThrow.test(notBookAssignedActivatedOnHold,
                                new EqmValidationException(localizedMessageBuilder, WRONG_EQUIPMENT_STATUS, cpe.getStatus()));
                        Lambdas.verifyOrThrow.test(notActivatedAssigned && !Objects.equals(dto.getServiceId(), cpe.getServiceId()),
                                new EqmValidationException(localizedMessageBuilder, CPE_SERVICE_CANNOT_BE_UPDATED_ASSIGNED_OR_ACTIVATED));
                        cpe.setServiceId(finalServiceId);
                    });
        }

        Lambdas.verifyOrThrow.test((dto.getServiceId() == null && cpe.getServiceId() == null) && (cpe.getStatus().equals(Status.ACTIVATED) || cpe.getStatus().equals(Status.ASSIGNED)),
                new EqmValidationException(localizedMessageBuilder, CPE_SERVICE_NOTNULL_ASSIGNED_OR_ACTIVATED));
        Lambdas.verifyOrThrow.test(StringUtils.isNoneBlank(dto.getOrderId()) && !cpe.getStatus().equals(Status.BOOKED),
                new EqmValidationException(localizedMessageBuilder, CPE_ORDERID_CHECK_STATUS_BOOKED));

        return cpe;
    }
}

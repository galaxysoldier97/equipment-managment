package mc.monacotelecom.tecrep.equipments.process.ancillary;

import mc.monacotelecom.inventory.common.Lambdas;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.assembler.AncillaryEquipmentResourceAssembler;
import mc.monacotelecom.tecrep.equipments.dto.AncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.UpdateAncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateAncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateAncillaryEquipmentDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.AncillaryEquipmentDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.entity.Equipment;
import mc.monacotelecom.tecrep.equipments.entity.Warehouse;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.importer.validators.AncillaryEquipmentValidator;
import mc.monacotelecom.tecrep.equipments.mapper.AncillaryMapper;
import mc.monacotelecom.tecrep.equipments.repository.AncillaryRepository;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentRepository;
import mc.monacotelecom.tecrep.equipments.repository.WarehouseRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.WAREHOUSE_ID_NOT_FOUND;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.WAREHOUSE_NAME_NOT_FOUND;

@Component
public class AncillaryUpdater {
    private final AncillaryRepository ancillaryRepository;
    private final AncillaryEquipmentResourceAssembler ancillaryEquipmentResourceAssembler;
    private final AncillaryEquipmentValidator ancillaryEquipmentValidator;
    private final WarehouseRepository warehouseRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final AncillaryMapper ancillaryMapper;

    public AncillaryUpdater(final AncillaryRepository ancillaryRepository,
                            final EquipmentRepository<Equipment> equipmentRepository,
                            final AncillaryEquipmentValidator ancillaryEquipmentValidator,
                            final WarehouseRepository warehouseRepository,
                            final LocalizedMessageBuilder localizedMessageBuilder,
                            final AncillaryMapper ancillaryMapper) {
        this.ancillaryRepository = ancillaryRepository;
        this.ancillaryEquipmentResourceAssembler = AncillaryEquipmentResourceAssembler.of(AncillaryUpdater.class, equipmentRepository, ancillaryMapper);
        this.ancillaryEquipmentValidator = ancillaryEquipmentValidator;
        this.warehouseRepository = warehouseRepository;
        this.localizedMessageBuilder = localizedMessageBuilder;
        this.ancillaryMapper = ancillaryMapper;
    }

    /**
     * Function that, receiving a set of optional parameters, updated the ancillaryEquipment
     *
     * @param id  : target id
     * @param dto : optional input parameters
     * @return : updated AncillaryEquipment
     */
    @Deprecated(since = "2.21.0", forRemoval = true)
    public AncillaryEquipmentDTO updateV1(final Long id, UpdateAncillaryEquipmentDTO dto) {
        AncillaryEquipment ancillary = commonUpdate(id, dto);

        final boolean isWareHousePresent = Objects.nonNull(dto.getWarehouse()) && Objects.nonNull(dto.getWarehouse().getWarehouseId());
        final Warehouse warehouse = isWareHousePresent ? warehouseRepository.findById(dto.getWarehouse().getWarehouseId()).orElse(null) : null;
        ancillary.setWarehouse(warehouse);

        return ancillaryEquipmentResourceAssembler.toModel(ancillary);
    }

    public AncillaryEquipmentDTOV2 update(Long id, UpdateAncillaryEquipmentDTOV2 dto) {
        AncillaryEquipment ancillary = commonUpdate(id, dto);

        if (StringUtils.isNotBlank(dto.getWarehouseName())) {
            ancillary.setWarehouse(warehouseRepository.findByName(dto.getWarehouseName())
                    .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, WAREHOUSE_NAME_NOT_FOUND, dto.getWarehouseName())));
        } else {
            ancillary.setWarehouse(null);
        }

        return ancillaryMapper.toDtoV2(ancillary);
    }

    private AncillaryEquipment commonUpdate(Long id, UpdateAncillaryEquipment dto) {
        var ancillary = ancillaryEquipmentValidator.checkBeforeUpdate(id, dto);

        ancillary.setMacAddress(dto.getMacAddress());
        ancillary.setOrderId(dto.getOrderId());
        ancillary.setIndependent(dto.getIndependent());
        ancillary.setSfpVersion(dto.getSfpVersion());
        ancillary.setBatchNumber(dto.getBatchNumber());
        ancillary.setExternalNumber(dto.getExternalNumber());
        ancillary.setServiceId(dto.getServiceId());

        return ancillaryRepository.save(ancillary);
    }

    /**
     * Update ancillary Equipment with only information filled in input DTO
     *
     * @param id  : ancillary equipment target id
     * @param dto : optional input parameters
     * @return : updated AncillaryEquipment
     */
    @Deprecated(since = "2.21.0", forRemoval = true)
    public AncillaryEquipmentDTO partialUpdateV1(final Long id, UpdateAncillaryEquipmentDTO dto) {
        AncillaryEquipment ancillary = commonPartialUpdate(id, dto);

        if (Objects.nonNull(dto.getWarehouse())) {
            final var warehouse = warehouseRepository.findById(dto.getWarehouse().getWarehouseId())
                    .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, WAREHOUSE_ID_NOT_FOUND, dto.getWarehouse().getWarehouseId()));
            ancillary.setWarehouse(warehouse);
        }

        return ancillaryEquipmentResourceAssembler.toModel(ancillary);
    }

    public AncillaryEquipmentDTOV2 partialUpdate(Long id, UpdateAncillaryEquipmentDTOV2 dto) {
        AncillaryEquipment ancillary = commonPartialUpdate(id, dto);

        if (StringUtils.isNotBlank(dto.getWarehouseName())) {
            final var warehouse = warehouseRepository.findByName(dto.getWarehouseName())
                    .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, WAREHOUSE_NAME_NOT_FOUND, dto.getWarehouseName()));
            ancillary.setWarehouse(warehouse);
        }

        return ancillaryMapper.toDtoV2(ancillary);
    }

    private AncillaryEquipment commonPartialUpdate(Long id, UpdateAncillaryEquipment dto) {
        var currentAncillaryEquipment = ancillaryEquipmentValidator.checkBeforeUpdate(id, dto);
        final var finalAncillaryEquipmentActual = currentAncillaryEquipment;
        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(dto.getMacAddress()), currentAncillaryEquipment::setMacAddress, dto.getMacAddress());

        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(dto.getOrderId()), currentAncillaryEquipment::setOrderId, dto.getOrderId());
        Lambdas.verifyAndApplyObj.accept(Objects.nonNull(dto.getIndependent()), i -> finalAncillaryEquipmentActual.setIndependent((Boolean) i), dto.getIndependent());
        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(dto.getSfpVersion()), currentAncillaryEquipment::setSfpVersion, dto.getSfpVersion());
        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(dto.getBatchNumber()), currentAncillaryEquipment::setBatchNumber, dto.getBatchNumber());
        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(dto.getExternalNumber()), currentAncillaryEquipment::setExternalNumber, dto.getExternalNumber());
        Lambdas.verifyAndApplyObj.accept(Objects.nonNull(dto.getServiceId()), i -> finalAncillaryEquipmentActual.setServiceId((Long) i), dto.getServiceId());

        return ancillaryRepository.save(currentAncillaryEquipment);
    }
}

package mc.monacotelecom.tecrep.equipments.process.ancillary;

import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.assembler.AncillaryEquipmentResourceAssembler;
import mc.monacotelecom.tecrep.equipments.dto.AncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v1.AddAncillaryDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddAncillaryDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.AncillaryEquipmentDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.entity.Equipment;
import mc.monacotelecom.tecrep.equipments.entity.Provider;
import mc.monacotelecom.tecrep.equipments.entity.Warehouse;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.mapper.AncillaryMapper;
import mc.monacotelecom.tecrep.equipments.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static mc.monacotelecom.tecrep.equipments.enums.EquipmentModelCategory.ANCILLARY;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.*;

@Component
public class AncillaryCreator {
    private final AncillaryRepository ancillaryRepository;
    private final AncillaryEquipmentResourceAssembler ancillaryEquipmentResourceAssembler;
    private final WarehouseRepository warehouseRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final ProviderRepository providerRepository;
    private final EquipmentModelRepository equipmentModelRepository;
    private final AncillaryMapper ancillaryMapper;

    public AncillaryCreator(final AncillaryRepository ancillaryRepository,
                            final EquipmentRepository<Equipment> equipmentRepository,
                            final WarehouseRepository warehouseRepository,
                            final LocalizedMessageBuilder localizedMessageBuilder,
                            final ProviderRepository providerRepository,
                            final EquipmentModelRepository equipmentModelRepository,
                            final AncillaryMapper ancillaryMapper) {
        this.ancillaryRepository = ancillaryRepository;
        this.ancillaryEquipmentResourceAssembler = AncillaryEquipmentResourceAssembler.of(AncillaryCreator.class, equipmentRepository, ancillaryMapper);
        this.warehouseRepository = warehouseRepository;
        this.localizedMessageBuilder = localizedMessageBuilder;
        this.providerRepository = providerRepository;
        this.equipmentModelRepository = equipmentModelRepository;
        this.ancillaryMapper = ancillaryMapper;
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public AncillaryEquipmentDTO addV1(AddAncillaryDTO dto) {
        Provider provider;
        Warehouse warehouse;

        if (dto.getWarehouse().getWarehouseId() != null) {
            warehouse = warehouseRepository.findById(dto.getWarehouse().getWarehouseId()).orElseThrow(() ->
                    new EqmValidationException(localizedMessageBuilder, WAREHOUSE_ID_NOT_FOUND, dto.getWarehouse().getWarehouseId()));
        } else {
            warehouse = warehouseRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, NO_WAREHOUSE));
        }

        // Prioritizing model.name over modelName since the migration from String to EquipmentModel
        final var modelName = Objects.nonNull(dto.getModel()) ? dto.getModel().getName() : dto.getModelName();
        var equipmentModel = equipmentModelRepository.findByNameAndCategory(modelName, ANCILLARY)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, MODEL_NOT_FOUND_NAME_CATEGORY, modelName, ANCILLARY));

        Optional<AncillaryEquipment> equipmentBySN = ancillaryRepository.findBySerialNumber(dto.getSerialNumber());
        if (equipmentBySN.isPresent()) {
            throw new EqmValidationException(localizedMessageBuilder, SERIAL_NUMBER_ALREADY_IN_USE, dto.getSerialNumber());
        }

        var ancillaryEquipment = ancillaryMapper.toEntity(dto);
        ancillaryEquipment.setStatus(Status.INSTORE);
        ancillaryEquipment.setWarehouse(warehouse);
        ancillaryEquipment.setModel(equipmentModel);

        return ancillaryEquipmentResourceAssembler.toModel(ancillaryRepository.save(ancillaryEquipment));
    }

    public AncillaryEquipmentDTOV2 add(AddAncillaryDTOV2 dto) {
        var ancillaryEquipment = ancillaryMapper.toEntity(dto);

        // Validate Serial Number
        Optional<AncillaryEquipment> equipmentBySN = ancillaryRepository.findBySerialNumber(dto.getSerialNumber());
        if (equipmentBySN.isPresent()) {
            throw new EqmValidationException(localizedMessageBuilder, SERIAL_NUMBER_ALREADY_IN_USE, dto.getSerialNumber());
        }

        // Validate Model
        ancillaryEquipment.setModel(equipmentModelRepository.findByNameAndCategory(dto.getModelName(), ANCILLARY)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, MODEL_NOT_FOUND_NAME_CATEGORY, dto.getModelName(), ANCILLARY)));

        // Validate Warehouse
        Warehouse warehouse;
        if (StringUtils.isNotBlank(dto.getWarehouseName())) {
            warehouse = warehouseRepository.findByName(dto.getWarehouseName()).orElseThrow(() ->
                    new EqmValidationException(localizedMessageBuilder, WAREHOUSE_NAME_NOT_FOUND, dto.getWarehouseName()));
        } else {
            warehouse = warehouseRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, NO_WAREHOUSE));
        }
        ancillaryEquipment.setWarehouse(warehouse);

        ancillaryEquipment.setStatus(Status.INSTORE);

        return ancillaryMapper.toDtoV2(ancillaryRepository.save(ancillaryEquipment));
    }
}

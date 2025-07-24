package mc.monacotelecom.tecrep.equipments.process.cpe;

import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.assembler.CPEResourceAssembler;
import mc.monacotelecom.tecrep.equipments.dto.CPEDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v1.AddCPEDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddCPEDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.CPEDTOV2;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentModelCategory;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.importer.validators.CPEValidators;
import mc.monacotelecom.tecrep.equipments.mapper.CPEMapper;
import mc.monacotelecom.tecrep.equipments.repository.CPERepository;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentModelRepository;
import mc.monacotelecom.tecrep.equipments.repository.WarehouseRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.MODEL_NOT_FOUND_NAME_CATEGORY;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.WAREHOUSE_ID_NOT_FOUND;

@Component
public class CPECreator {
    private final CPERepository cpeRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final WarehouseRepository warehouseRepository;
    private final EquipmentModelRepository equipmentModelRepository;
    private final CPEResourceAssembler cpeResourceAssembler;
    private final CPEValidators cpeValidators;
    private final CPEMapper cpeMapper;

    public CPECreator(final CPERepository cpeRepository,
                      final LocalizedMessageBuilder localizedMessageBuilder,
                      final WarehouseRepository warehouseRepository,
                      final EquipmentModelRepository equipmentModelRepository,
                      final CPEValidators cpeValidators,
                      final CPEMapper cpeMapper) {
        this.cpeRepository = cpeRepository;
        this.localizedMessageBuilder = localizedMessageBuilder;
        this.warehouseRepository = warehouseRepository;
        this.equipmentModelRepository = equipmentModelRepository;
        this.cpeValidators = cpeValidators;
        this.cpeMapper = cpeMapper;
        this.cpeResourceAssembler = CPEResourceAssembler.of(CPECreator.class, cpeMapper);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public CPEDTO addV1(AddCPEDTO dto) {
        var warehouse = warehouseRepository.findById(dto.getWarehouse().getWarehouseId())
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, WAREHOUSE_ID_NOT_FOUND, dto.getWarehouse().getWarehouseId()));

        // Prioritizing model.name over modelName since the migration from String to EquipmentModel
        final var modelName = Objects.nonNull(dto.getModel()) ? dto.getModel().getName() : dto.getModelName();
        var equipmentModel = equipmentModelRepository.findByNameAndCategory(modelName, EquipmentModelCategory.CPE)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, MODEL_NOT_FOUND_NAME_CATEGORY, modelName, EquipmentModelCategory.CPE));

        cpeValidators.validateCpeAttributeExistenceBeforeAdd(dto);
        var cpe = cpeMapper.toEntity(dto);
        cpe.setStatus(Status.INSTORE);
        cpe.setRecyclable(true);
        cpe.setWarehouse(warehouse);
        cpe.setModel(equipmentModel);

        return cpeResourceAssembler.toModel(cpeRepository.save(cpe));
    }

    public CPEDTOV2 add(AddCPEDTOV2 dto) {
        var warehouse = warehouseRepository.findByName(dto.getWarehouseName())
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, WAREHOUSE_ID_NOT_FOUND, dto.getWarehouseName()));

        var equipmentModel = equipmentModelRepository.findByNameAndCategory(dto.getModelName(), EquipmentModelCategory.CPE)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, MODEL_NOT_FOUND_NAME_CATEGORY, dto.getModelName(), EquipmentModelCategory.CPE));

        cpeValidators.validateCpeAttributeExistenceBeforeAdd(dto);
        var cpe = cpeMapper.toEntity(dto);
        cpe.setStatus(Status.INSTORE);
        cpe.setRecyclable(true);
        cpe.setWarehouse(warehouse);
        cpe.setModel(equipmentModel);

        return cpeMapper.toDtoV2(cpeRepository.save(cpe));
    }
}

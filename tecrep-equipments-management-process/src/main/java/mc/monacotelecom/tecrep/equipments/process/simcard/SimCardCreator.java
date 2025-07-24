package mc.monacotelecom.tecrep.equipments.process.simcard;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.inventory.common.Lambdas;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.dto.SimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v1.AddSimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddSimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.SimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.*;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentCategory;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmConflictException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.mapper.SimCardMapper;
import mc.monacotelecom.tecrep.equipments.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.*;

@Component
@RequiredArgsConstructor
public class SimCardCreator {
    private final SimCardRepository simCardRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final PlmnRepository plmnRepository;
    private final InventoryPoolRepository inventoryPoolRepository;
    private final ProviderRepository providerRepository;
    private final WarehouseRepository warehouseRepository;
    private final SimCardMapper simCardMapper;

    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO createAndMapV1(AddSimCardDTO dto) {
        return simCardMapper.toDtoV1(simCardRepository.save(createV1(dto)));
    }

    public SimCardDTOV2 createAndMap(AddSimCardDTOV2 dto) {
        return simCardMapper.toDtoV2(simCardRepository.save(create(dto)));
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCard createV1(AddSimCardDTO dto) {
        Provider provider = providerRepository.findById(dto.getProvider().getProviderId())
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, PROVIDER_ID_NOT_FOUND, dto.getProvider().getProviderId()));

        Plmn plmn = plmnRepository.findById(dto.getPlmn().getPlmnId())
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, PLMN_ID_NOT_FOUND, dto.getPlmn().getPlmnId()));

        if (Objects.nonNull(dto.getSerialNumber())) {
            Optional<SimCard> matchingSimCard = simCardRepository.findBySerialNumberAndCategory(dto.getSerialNumber(), EquipmentCategory.SIMCARD);
            if (matchingSimCard.isPresent()) {
                throw new EqmConflictException(localizedMessageBuilder, SIMCARD_SERIAL_NUMBER_ALREADY_IN_USE, dto.getSerialNumber(), matchingSimCard.get().getEquipmentId());
            }
        }

        InventoryPool inventoryPool = null;
        if (dto.getInventoryPool() != null) {
            inventoryPool = inventoryPoolRepository.findByCode(dto.getInventoryPool().getCode()).orElseThrow(
                    () -> new EqmValidationException(localizedMessageBuilder, INVENTORY_POOL_NOT_FOUND_CODE, dto.getInventoryPool().getCode()));
        }

        SimCard newSimCard = simCardMapper.toEntity(dto);
        newSimCard.setProvider(provider);
        newSimCard.setPlmn(plmn);
        newSimCard.setInventoryPool(inventoryPool);

        Lambdas.verifyOrThrow.test(dto.getImsiNumber().length() != 15,
                new EqmValidationException(localizedMessageBuilder, IMSI_EXCEEDED_SIZE, dto.getImsiNumber(), 15));

        newSimCard.setServiceId(null);
        if (dto.getWarehouse() != null) {
            Warehouse warehouse = warehouseRepository.findById(dto.getWarehouse().getWarehouseId())
                    .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, WAREHOUSE_ID_NOT_FOUND, dto.getWarehouse().getWarehouseId()));

            newSimCard.setWarehouse(warehouse);
        }

        newSimCard.setStatus(Status.INSTORE);
        newSimCard.setCategory(EquipmentCategory.SIMCARD);
        newSimCard.setRecyclable(false);
        if (inventoryPool != null) {
            newSimCard.setSimProfile(inventoryPool.getSimProfile().name());
        }

        return simCardRepository.save(newSimCard);
    }

    public SimCard create(AddSimCardDTOV2 dto) {
        SimCard newSimCard = simCardMapper.toEntity(dto);

        // Validate ICCID
        if (Objects.nonNull(dto.getSerialNumber())) {
            Optional<SimCard> matchingSimCard = simCardRepository.findBySerialNumberAndCategory(dto.getSerialNumber(), EquipmentCategory.SIMCARD);
            if (matchingSimCard.isPresent()) {
                throw new EqmConflictException(localizedMessageBuilder, SIMCARD_SERIAL_NUMBER_ALREADY_IN_USE, dto.getSerialNumber(), matchingSimCard.get().getEquipmentId());
            }
        }

        // Validate IMSI
        Lambdas.verifyOrThrow.test(dto.getImsiNumber().length() != 15,
                new EqmValidationException(localizedMessageBuilder, IMSI_EXCEEDED_SIZE, dto.getImsiNumber(), 15));

        // Validate Provider
        newSimCard.setProvider(providerRepository.findByName(dto.getProviderName())
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, PROVIDER_NOT_FOUND_NAME, dto.getProviderName())));

        // Validate PLMN
        newSimCard.setPlmn(plmnRepository.findByCode(dto.getPlmnCode())
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, PLMN_NOT_FOUND_CODE, dto.getPlmnCode())));

        // Validate Inventory Pool
        if (StringUtils.isNotBlank(dto.getInventoryPoolCode())) {
            InventoryPool inventoryPool = inventoryPoolRepository.findByCode(dto.getInventoryPoolCode()).orElseThrow(
                    () -> new EqmValidationException(localizedMessageBuilder, INVENTORY_POOL_NOT_FOUND_CODE, dto.getInventoryPoolCode()));
            newSimCard.setInventoryPool(inventoryPool);
            newSimCard.setSimProfile(inventoryPool.getSimProfile().name());
        }

        // Validate Warehouse
        if (StringUtils.isNotBlank(dto.getWarehouseName())) {
            Warehouse warehouse = warehouseRepository.findByName(dto.getWarehouseName())
                    .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, WAREHOUSE_NAME_NOT_FOUND, dto.getWarehouseName()));
            newSimCard.setWarehouse(warehouse);
        }

        newSimCard.setServiceId(null);
        newSimCard.setStatus(Status.INSTORE);
        newSimCard.setCategory(EquipmentCategory.SIMCARD);
        newSimCard.setRecyclable(false);

        return simCardRepository.save(newSimCard);
    }
}

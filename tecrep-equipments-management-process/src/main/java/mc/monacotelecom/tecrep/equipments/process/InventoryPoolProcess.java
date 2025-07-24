package mc.monacotelecom.tecrep.equipments.process;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.inventory.common.CommonFunctions;
import mc.monacotelecom.inventory.common.Lambdas;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.dto.InventoryPoolDTO;
import mc.monacotelecom.tecrep.equipments.dto.InventoryPoolDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v1.AddInventoryPoolDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddInventoryPoolDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchInventoryPoolDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.UpdateInventoryPoolDTO;
import mc.monacotelecom.tecrep.equipments.entity.InventoryPool;
import mc.monacotelecom.tecrep.equipments.enums.SimCardSimProfile;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmConflictException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmNotFoundException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.mapper.InventoryPoolMapper;
import mc.monacotelecom.tecrep.equipments.repository.InventoryPoolRepository;
import mc.monacotelecom.tecrep.equipments.repository.specification.InventoryPoolSpecifications;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.*;

@Component
@RequiredArgsConstructor
public class InventoryPoolProcess {

    private final InventoryPoolRepository inventoryPoolRepository;
    private final InventoryPoolMapper inventoryPoolMapper;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    @Deprecated(forRemoval = true, since = "2.21")
    public Page<InventoryPoolDTO> search(final SearchInventoryPoolDTO searchInventoryPoolDTO, final Pageable pageable) {
        return inventoryPoolRepository.findAll(prepareSpecifications(searchInventoryPoolDTO), pageable).map(inventoryPoolMapper::toDto);
    }

    public Page<InventoryPoolDTOV2> searchV2(final SearchInventoryPoolDTO searchInventoryPoolDTO, final Pageable pageable) {
        return inventoryPoolRepository.findAll(prepareSpecifications(searchInventoryPoolDTO), pageable).map(inventoryPoolMapper::toDtoV2);
    }

    private Specification<InventoryPool> prepareSpecifications(final SearchInventoryPoolDTO searchInventoryPoolDTO) {
        Specification<InventoryPool> specification = null;

        specification = StringUtils.isNotBlank(searchInventoryPoolDTO.getCode()) ? Specification.where(InventoryPoolSpecifications.hasCode(searchInventoryPoolDTO.getCode())) : specification;
        specification = Objects.nonNull(searchInventoryPoolDTO.getMvno()) ? CommonFunctions.addSpecification(specification, InventoryPoolSpecifications.hasMvno(searchInventoryPoolDTO.getMvno())) : specification;
        specification = Objects.nonNull(searchInventoryPoolDTO.getSimProfile()) ? CommonFunctions.addSpecification(specification, InventoryPoolSpecifications.hasSimProfile(searchInventoryPoolDTO.getSimProfile())) : specification;

        return specification;
    }

    @Deprecated(forRemoval = true, since = "2.21")
    public InventoryPoolDTO getByCode(final String code) {
        final var inventoryPool = inventoryPoolRepository.findByCode(code)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, INVENTORY_POOL_NOT_FOUND_CODE, code));
        return inventoryPoolMapper.toDto(inventoryPool);
    }

    public InventoryPoolDTOV2 getByCodeV2(final String code) {
        final var inventoryPool = inventoryPoolRepository.findByCode(code)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, INVENTORY_POOL_NOT_FOUND_CODE, code));
        return inventoryPoolMapper.toDtoV2(inventoryPool);
    }

    public void deleteByCode(final String code) {
        final var inventoryPool = inventoryPoolRepository.findByCode(code)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, INVENTORY_POOL_NOT_FOUND_CODE, code));

        inventoryPool.getSimCards().stream().findFirst().ifPresent(simCard -> {
            throw new EqmValidationException(localizedMessageBuilder, INVENTORY_POOL_NOT_DELETABLE_SIMCARD, code, simCard.getSerialNumber());
        });

        inventoryPool.getBatches().stream().findFirst().ifPresent(batch -> {
            throw new EqmValidationException(localizedMessageBuilder, INVENTORY_POOL_NOT_DELETABLE_BATCH, code, batch.getBatchNumber());
        });

        inventoryPool.getAllotments().stream().findFirst().ifPresent(allotment -> {
            throw new EqmValidationException(localizedMessageBuilder, INVENTORY_POOL_NOT_DELETABLE_ALLOTMENT, code, allotment.getAllotmentId());
        });

        inventoryPoolRepository.deleteByCode(code);
    }

    @Deprecated(forRemoval = true, since = "2.21")
    public InventoryPoolDTO create(final AddInventoryPoolDTO inventoryPoolDTO) {
        if (inventoryPoolRepository.existsByCode(inventoryPoolDTO.getCode())) {
            throw new EqmConflictException(localizedMessageBuilder, INVENTORY_POOL_CODE_ALREADY_EXISTS, inventoryPoolDTO.getCode());
        }

        return inventoryPoolMapper.toDto(inventoryPoolRepository.save(inventoryPoolMapper.toEntity(inventoryPoolDTO)));
    }

    public InventoryPoolDTOV2 createV2(final AddInventoryPoolDTOV2 inventoryPoolDTO) {
        if (inventoryPoolRepository.existsByCode(inventoryPoolDTO.getCode())) {
            throw new EqmConflictException(localizedMessageBuilder, INVENTORY_POOL_CODE_ALREADY_EXISTS, inventoryPoolDTO.getCode());
        }

        return inventoryPoolMapper.toDtoV2(inventoryPoolRepository.save(inventoryPoolMapper.toEntity(inventoryPoolDTO)));
    }

    @Deprecated(forRemoval = true, since = "2.21")
    public InventoryPoolDTO update(final String code, final UpdateInventoryPoolDTO inventoryPoolDTO) {
        return inventoryPoolMapper.toDto(inventoryPoolRepository.save(commonUpdate(code, inventoryPoolDTO)));
    }

    public InventoryPoolDTOV2 updateV2(final String code, final UpdateInventoryPoolDTO inventoryPoolDTO) {
        return inventoryPoolMapper.toDtoV2(inventoryPoolRepository.save(commonUpdate(code, inventoryPoolDTO)));
    }

    private InventoryPool commonUpdate(String code, UpdateInventoryPoolDTO inventoryPoolDTO) {
        final var inventoryPool = inventoryPoolRepository.findByCode(code)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, INVENTORY_POOL_NOT_FOUND_CODE, code));

        final var existingInventoryPoolWithNewCode = inventoryPoolRepository.findByCode(inventoryPoolDTO.getCode());
        if (existingInventoryPoolWithNewCode.isPresent() && !existingInventoryPoolWithNewCode.get().getInventoryPoolId().equals(inventoryPool.getInventoryPoolId())) {
            throw new EqmConflictException(localizedMessageBuilder, INVENTORY_POOL_CODE_ALREADY_EXISTS, inventoryPoolDTO.getCode());
        }

        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(inventoryPoolDTO.getCode()), inventoryPool::setCode, inventoryPoolDTO.getCode());
        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(inventoryPoolDTO.getDescription()), inventoryPool::setDescription, inventoryPoolDTO.getDescription());
        Lambdas.verifyAndApplyObj.accept(Objects.nonNull(inventoryPoolDTO.getMvno()), mvno -> inventoryPool.setMvno((Integer) mvno), inventoryPoolDTO.getMvno());
        Lambdas.verifyAndApplyObj.accept(Objects.nonNull(inventoryPoolDTO.getSimProfile()), simProfile -> inventoryPool.setSimProfile((SimCardSimProfile) simProfile), inventoryPoolDTO.getSimProfile());
        return inventoryPool;
    }
}

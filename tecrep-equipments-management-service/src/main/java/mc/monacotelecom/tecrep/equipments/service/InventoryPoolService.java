package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.InventoryPoolDTO;
import mc.monacotelecom.tecrep.equipments.dto.InventoryPoolDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v1.AddInventoryPoolDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddInventoryPoolDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchInventoryPoolDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.UpdateInventoryPoolDTO;
import mc.monacotelecom.tecrep.equipments.process.InventoryPoolProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryPoolService {

    private final InventoryPoolProcess inventoryPoolProcess;

    @Transactional(readOnly = true)
    @Deprecated(forRemoval = true, since = "2.21")
    public Page<InventoryPoolDTO> search(final SearchInventoryPoolDTO searchInventoryPoolDTO, final Pageable pageable) {
        return inventoryPoolProcess.search(searchInventoryPoolDTO, pageable);
    }

    @Transactional(readOnly = true)
    public Page<InventoryPoolDTOV2> searchV2(final SearchInventoryPoolDTO searchInventoryPoolDTO, final Pageable pageable) {
        return inventoryPoolProcess.searchV2(searchInventoryPoolDTO, pageable);
    }

    @Transactional(readOnly = true)
    @Deprecated(forRemoval = true, since = "2.21")
    public InventoryPoolDTO getByCode(final String code) {
        return inventoryPoolProcess.getByCode(code);
    }

    @Transactional(readOnly = true)
    public InventoryPoolDTOV2 getByCodeV2(final String code) {
        return inventoryPoolProcess.getByCodeV2(code);
    }

    @Transactional
    public void deleteByCode(final String code) {
        inventoryPoolProcess.deleteByCode(code);
    }

    @Transactional
    @Deprecated(forRemoval = true, since = "2.21")
    public InventoryPoolDTO create(final AddInventoryPoolDTO inventoryPoolDTO) {
        return inventoryPoolProcess.create(inventoryPoolDTO);
    }

    @Transactional
    public InventoryPoolDTOV2 createV2(final AddInventoryPoolDTOV2 inventoryPoolDTO) {
        return inventoryPoolProcess.createV2(inventoryPoolDTO);
    }

    @Transactional
    @Deprecated(forRemoval = true, since = "2.21")
    public InventoryPoolDTO update(final String code, final UpdateInventoryPoolDTO inventoryPoolDTO) {
        return inventoryPoolProcess.update(code, inventoryPoolDTO);
    }

    @Transactional
    public InventoryPoolDTOV2 updateV2(final String code, final UpdateInventoryPoolDTO inventoryPoolDTO) {
        return inventoryPoolProcess.updateV2(code, inventoryPoolDTO);
    }
}

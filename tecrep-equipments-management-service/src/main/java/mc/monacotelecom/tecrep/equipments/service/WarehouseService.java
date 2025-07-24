package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.WarehouseDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchWarehouseDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.WarehouseDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.Warehouse;
import mc.monacotelecom.tecrep.equipments.process.WarehouseProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class WarehouseService {
    final WarehouseProcess wareHouseProcess;

    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<WarehouseDTO> searchV1(SearchWarehouseDTO searchWarehouseDTO, Pageable pageable, PagedResourcesAssembler<Warehouse> assembler) {
        return wareHouseProcess.searchV1(searchWarehouseDTO, pageable, assembler);
    }

    @Transactional(readOnly = true)
    public Page<WarehouseDTOV2> search(SearchWarehouseDTO searchWarehouseDTO, Pageable pageable) {
        return wareHouseProcess.search(searchWarehouseDTO, pageable);
    }

    @Transactional
    public void delete(Long warehouseId) {
        wareHouseProcess.delete(warehouseId);
    }

    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<WarehouseDTO> getAll(Pageable pageable, PagedResourcesAssembler<Warehouse> assembler) {
        return wareHouseProcess.getAll(pageable, assembler);
    }

    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public WarehouseDTO updateV1(Long warehouseId, WarehouseDTO warehouseDTO) {
        return wareHouseProcess.updateV1(warehouseId, warehouseDTO);
    }

    @Transactional
    public WarehouseDTOV2 update(Long warehouseId, WarehouseDTOV2 warehouseDTO) {
        return wareHouseProcess.update(warehouseId, warehouseDTO);
    }

    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public WarehouseDTO addV1(WarehouseDTO warehouseDTO) {
        return wareHouseProcess.addV1(warehouseDTO);
    }

    @Transactional
    public WarehouseDTOV2 add(WarehouseDTOV2 warehouseDTO) {
        return wareHouseProcess.add(warehouseDTO);
    }

    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public WarehouseDTO getByIdV1(Long warehouseId) {
        return wareHouseProcess.getByIdV1(warehouseId);
    }

    @Transactional(readOnly = true)
    public WarehouseDTOV2 getById(Long warehouseId) {
        return wareHouseProcess.getById(warehouseId);
    }
}

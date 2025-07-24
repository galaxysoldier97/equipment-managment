package mc.monacotelecom.tecrep.equipments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.WarehouseDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchWarehouseDTO;
import mc.monacotelecom.tecrep.equipments.entity.Warehouse;
import mc.monacotelecom.tecrep.equipments.service.WarehouseService;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Warehouse API V1")
@CrossOrigin
@RestController
@RequestMapping({"private/auth/warehouses", "api/v1/private/auth/warehouses"})
@RequiredArgsConstructor
@Deprecated(since = "2.21.0", forRemoval = true)
public class WarehouseController {

    private final WarehouseService warehouseService;

    @Operation(summary = "Find a warehouse by Id", operationId = "getWarehouseById")
    @ApiResponse(responseCode = "200", description = "Warehouse found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @Deprecated(since = "2.21.0", forRemoval = true)
    @GetMapping("/{warehouseId}")
    public WarehouseDTO getById(@PathVariable("warehouseId") Long warehouseId) {
        return warehouseService.getByIdV1(warehouseId);
    }

    @Operation(summary = "Add a warehouse", operationId = "addWarehouse")
    @ApiResponse(responseCode = "201", description = "Warehouse created")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @Deprecated(since = "2.21.0", forRemoval = true)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WarehouseDTO add(@Valid @RequestBody WarehouseDTO warehouseDTO) {
        return warehouseService.addV1(warehouseDTO);
    }

    @Operation(summary = "Update a warehouse", operationId = "updateWarehouse")
    @ApiResponse(responseCode = "200", description = "Warehouse updated")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @Deprecated(since = "2.21.0", forRemoval = true)
    @PutMapping("/{warehouseId}")
    public WarehouseDTO update(@PathVariable("warehouseId") Long warehouseId, @Valid @RequestBody WarehouseDTO warehouseDTO) {
        return warehouseService.updateV1(warehouseId, warehouseDTO);
    }

    @Operation(summary = "Get all warehouses", operationId = "getAllWarehouses")
    @GetMapping
    @Deprecated(since = "2.21.0", forRemoval = true)
    @PageableAsQueryParam
    public PagedModel<WarehouseDTO> getAll(@Parameter(hidden = true) Pageable pageable, @Parameter(hidden = true) PagedResourcesAssembler<Warehouse> assembler) {
        return warehouseService.getAll(pageable, assembler);
    }

    @Operation(summary = "Delete a warehouse by id", operationId = "deleteWarehouseById")
    @Deprecated(since = "2.21.0", forRemoval = true)
    @DeleteMapping("/{warehouseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("warehouseId") Long warehouseId) {
        warehouseService.delete(warehouseId);
    }

    @Operation(summary = "Search a warehouse by code, code,...", operationId = "searchWarehouse")
    @PageableAsQueryParam
    @Deprecated(since = "2.21.0", forRemoval = true)
    @GetMapping("/search")
    public PagedModel<WarehouseDTO> search(SearchWarehouseDTO searchWarehouseDTO, @Parameter(hidden = true) Pageable pageable, @Parameter(hidden = true) PagedResourcesAssembler<Warehouse> assembler) {
        return warehouseService.searchV1(searchWarehouseDTO, pageable, assembler);
    }
}
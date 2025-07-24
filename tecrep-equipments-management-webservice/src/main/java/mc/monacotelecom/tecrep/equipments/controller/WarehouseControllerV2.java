package mc.monacotelecom.tecrep.equipments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchWarehouseDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.WarehouseDTOV2;
import mc.monacotelecom.tecrep.equipments.service.WarehouseService;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Warehouse API V2")
@CrossOrigin
@RestController
@RequestMapping("api/v2/private/auth/warehouses")
@RequiredArgsConstructor
public class WarehouseControllerV2 {

    private final WarehouseService warehouseService;

    @Operation(summary = "Find a warehouse by ID")
    @ApiResponse(responseCode = "200", description = "Warehouse found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @GetMapping("/{id}")
    public WarehouseDTOV2 getById(@PathVariable("id") Long id) {
        return warehouseService.getById(id);
    }

    @Operation(summary = "Add a warehouse")
    @ApiResponse(responseCode = "201", description = "Warehouse created")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WarehouseDTOV2 add(@Valid @RequestBody WarehouseDTOV2 dto) {
        return warehouseService.add(dto);
    }

    @Operation(summary = "Update a warehouse")
    @ApiResponse(responseCode = "200", description = "Warehouse updated")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PutMapping("/{id}")
    public WarehouseDTOV2 update(@PathVariable("id") Long id,
                                 @Valid @RequestBody WarehouseDTOV2 dto) {
        return warehouseService.update(id, dto);
    }

    @Operation(summary = "Delete a warehouse by ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") Long id) {
        warehouseService.delete(id);
    }

    @Operation(summary = "Search a warehouse by several criteria")
    @PageableAsQueryParam
    @GetMapping
    public Page<WarehouseDTOV2> search(SearchWarehouseDTO dto, @Parameter(hidden = true) Pageable pageable) {
        return warehouseService.search(dto, pageable);
    }
}
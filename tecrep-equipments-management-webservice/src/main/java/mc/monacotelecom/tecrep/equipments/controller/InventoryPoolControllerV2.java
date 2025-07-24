package mc.monacotelecom.tecrep.equipments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.InventoryPoolDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddInventoryPoolDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchInventoryPoolDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.UpdateInventoryPoolDTO;
import mc.monacotelecom.tecrep.equipments.service.InventoryPoolService;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Inventory Pool API V2")
@CrossOrigin
@RestController
@RequestMapping({"api/v2/private/auth/inventorypools"})
@RequiredArgsConstructor
public class InventoryPoolControllerV2 {

    private final InventoryPoolService inventoryPoolService;

    @Operation(summary = "Search Inventory Pools by several fields", operationId = "searchInventoryPool")
    @ApiResponse(responseCode = "200", description = "Matching Inventory Pools returned")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PageableAsQueryParam
    @GetMapping
    public Page<InventoryPoolDTOV2> search(final SearchInventoryPoolDTO searchInventoryPoolDTO, @Parameter(hidden = true) final Pageable pageable) {
        return inventoryPoolService.searchV2(searchInventoryPoolDTO, pageable);
    }

    @Operation(summary = "Get Inventory Pool by code", operationId = "getInventoryPoolByCode")
    @ApiResponse(responseCode = "200", description = "Inventory Pool returned")
    @ApiResponse(responseCode = "404", description = "Inventory Pool does not exist")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @GetMapping("/{code}")
    public InventoryPoolDTOV2 getByCode(final @PathVariable("code") String code) {
        return inventoryPoolService.getByCodeV2(code);
    }

    @Operation(summary = "Delete Inventory Pool by code", operationId = "deleteInventoryPoolByCode")

    @DeleteMapping("/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByCode(final @PathVariable("code") String code) {
        inventoryPoolService.deleteByCode(code);
    }

    @Operation(summary = "Create Inventory Pool", operationId = "createInventoryPool")
    @ApiResponse(responseCode = "201", description = "Inventory Pool created")
    @ApiResponse(responseCode = "400", description = "Inventory Pool cannot be created")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryPoolDTOV2 create(final @Valid @RequestBody AddInventoryPoolDTOV2 inventoryPoolDTO) {
        return inventoryPoolService.createV2(inventoryPoolDTO);
    }

    @Operation(summary = "Partial update Inventory Pool", operationId = "updateInventoryPool")
    @ApiResponse(responseCode = "200", description = "Inventory Pool updated")
    @ApiResponse(responseCode = "400", description = "Inventory Pool cannot be updated")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PatchMapping("/{code}")
    public InventoryPoolDTOV2 update(final @Valid @RequestBody UpdateInventoryPoolDTO inventoryPoolDTO,
                                     final @PathVariable("code") String code) {
        return inventoryPoolService.updateV2(code, inventoryPoolDTO);
    }
}

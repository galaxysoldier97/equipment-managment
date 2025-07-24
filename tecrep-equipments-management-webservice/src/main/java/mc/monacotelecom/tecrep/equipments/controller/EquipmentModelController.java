package mc.monacotelecom.tecrep.equipments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.EquipmentModelDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.EquipmentModelCreateDTO;
import mc.monacotelecom.tecrep.equipments.service.EquipmentModelService;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Equipment Model API V1")
@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping({"private/auth/equipmentModels", "api/v1/private/auth/equipmentModels"})
@Deprecated(since = "2.21.0", forRemoval = true)
public class EquipmentModelController {

    private final EquipmentModelService equipmentModelService;

    @Operation(summary = "Get equipment models", operationId = "getEquipmentModels")
    @ApiResponse(responseCode = "200", description = "Equipment models retrieved")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PageableAsQueryParam
    @Deprecated(since = "2.21.0", forRemoval = true)
    @GetMapping
    public Page<EquipmentModelDTO> getAll(@Parameter(hidden = true) Pageable pageable) {
        return equipmentModelService.getAllV1(pageable);
    }

    @Operation(summary = "Find a equipmentModel by Id", operationId = "getEquipmentModelById")
    @ApiResponse(responseCode = "200", description = "Equipment model retrieved")
    @ApiResponse(responseCode = "404", description = "Equipment model not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @Deprecated(since = "2.21.0", forRemoval = true)
    @GetMapping("/{id}")
    public EquipmentModelDTO getById(@PathVariable final long id) {
        return equipmentModelService.getByIdV1(id);
    }

    @Operation(summary = "Create an equipment model", operationId = "createEquipmentModel")
    @ApiResponse(responseCode = "201", description = "Equipment model created")
    @ApiResponse(responseCode = "400", description = "There is an error with the given parameters")
    @ApiResponse(responseCode = "404", description = "Some of the required objects were not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @Deprecated(since = "2.21.0", forRemoval = true)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EquipmentModelDTO create(@RequestBody EquipmentModelCreateDTO equipmentModelDTO) {
        return equipmentModelService.createV1(equipmentModelDTO);
    }

    @Operation(summary = "Update an equipment model by Id", operationId = "updateEquipmentModel")
    @ApiResponse(responseCode = "200", description = "Equipment model updated")
    @ApiResponse(responseCode = "400", description = "There is an error with the given parameters")
    @ApiResponse(responseCode = "404", description = "Some of the required objects were not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @Deprecated(since = "2.21.0", forRemoval = true)
    @PutMapping("/{id}")
    public EquipmentModelDTO update(@PathVariable final long id, @RequestBody EquipmentModelCreateDTO equipmentModelCreateDTO) {
        return equipmentModelService.updateV1(id, equipmentModelCreateDTO);
    }

    @Operation(summary = "Delete an equipment model by Id", operationId = "deleteEquipmentModel")
    @ApiResponse(responseCode = "204", description = "Equipment model deleted")
    @ApiResponse(responseCode = "404", description = "Equipment model not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @Deprecated(since = "2.21.0", forRemoval = true)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final long id) {
        equipmentModelService.delete(id);
    }
}

package mc.monacotelecom.tecrep.equipments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.request.EquipmentModelCreateDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchEquipmentModelDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.EquipmentModelDTOV2;
import mc.monacotelecom.tecrep.equipments.service.EquipmentModelService;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Equipment Model API V2")
@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("api/v2/private/auth/equipmentModels")
public class EquipmentModelControllerV2 {

    private final EquipmentModelService equipmentModelService;

    @Operation(summary = "Search equipment models by several criteria")
    @ApiResponse(responseCode = "200", description = "Equipment models retrieved")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PageableAsQueryParam
    @GetMapping
    public Page<EquipmentModelDTOV2> search(final SearchEquipmentModelDTO dto, @Parameter(hidden = true) Pageable pageable) {
        return equipmentModelService.search(dto, pageable);
    }

    @Operation(summary = "Find an equipment model by ID")
    @ApiResponse(responseCode = "200", description = "Equipment model retrieved")
    @ApiResponse(responseCode = "404", description = "Equipment model not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @GetMapping("/{id}")
    public EquipmentModelDTOV2 getById(@PathVariable final long id) {
        return equipmentModelService.getById(id);
    }

    @Operation(summary = "Create an equipment model")
    @ApiResponse(responseCode = "201", description = "Equipment model created")
    @ApiResponse(responseCode = "400", description = "There is an error with the given parameters")
    @ApiResponse(responseCode = "404", description = "Some of the required objects were not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EquipmentModelDTOV2 create(@RequestBody EquipmentModelCreateDTO equipmentModelDTO) {
        return equipmentModelService.create(equipmentModelDTO);
    }

    @Operation(summary = "Update an equipment model by ID")
    @ApiResponse(responseCode = "200", description = "Equipment model updated")
    @ApiResponse(responseCode = "400", description = "There is an error with the given parameters")
    @ApiResponse(responseCode = "404", description = "Some of the required objects were not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PutMapping("/{id}")
    public EquipmentModelDTOV2 update(@PathVariable final long id, @RequestBody EquipmentModelCreateDTO equipmentModelCreateDTO) {
        return equipmentModelService.update(id, equipmentModelCreateDTO);
    }

    @Operation(summary = "Delete an equipment model by ID")
    @ApiResponse(responseCode = "204", description = "Equipment model deleted")
    @ApiResponse(responseCode = "404", description = "Equipment model not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final long id) {
        equipmentModelService.delete(id);
    }
}

package mc.monacotelecom.tecrep.equipments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.EquipmentConfigDTO;
import mc.monacotelecom.tecrep.equipments.entity.EquipmentConfig;
import mc.monacotelecom.tecrep.equipments.service.EquipmentConfigService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Equipment Config API")
@CrossOrigin
@RestController
@RequestMapping("api/v1/private/auth/equipmentConfigs")
@RequiredArgsConstructor
public class EquipmentConfigController {

    private final EquipmentConfigService service;

    @Operation(summary = "Get all equipment configs")
    @ApiResponse(responseCode = "200", description = "Equipment configs returned")
    @GetMapping
    public List<EquipmentConfigDTO> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Get equipment config by name and status")
    @ApiResponse(responseCode = "200", description = "Equipment config returned")
    @GetMapping("/search")
    public ResponseEntity<?> getByNameAndStatus(@RequestParam String name,
                                                @RequestParam String status) {
        boolean nameExists = service.existsByName(name);
        EquipmentConfig.Status enumStatus;
        try {
            enumStatus = EquipmentConfig.Status.valueOf(status);
        } catch (IllegalArgumentException e) {
            String message = nameExists ?
                    String.format("status (%s) no encontrado", status) :
                    String.format("name (%s) y status (%s) no encontrados", name, status);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", message));
        }

        EquipmentConfigDTO dto = service.getByNameAndStatus(name, enumStatus);
        if (dto == null) {
            String message = nameExists ?
                    String.format("name (%s) y status (%s) no encontrados", name, status) :
                    String.format("name (%s) no encontrado", name);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", message));
        }
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Create equipment config")
    @ApiResponse(responseCode = "201", description = "Equipment config created")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody EquipmentConfigDTO dto) {
        EquipmentConfigDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "created", "equipmentConfig", created));
    }

    @Operation(summary = "Update equipment config")
    @ApiResponse(responseCode = "200", description = "Equipment config updated")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody EquipmentConfigDTO dto) {
        EquipmentConfigDTO updated = service.update(id, dto);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "not found"));
        }
        return ResponseEntity.ok(Map.of("message", "updated", "equipmentConfig", updated));
    }

    @Operation(summary = "Delete equipment config")
    @ApiResponse(responseCode = "200", description = "Equipment config deleted")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        boolean deleted = service.delete(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "not found"));
        }
        return ResponseEntity.ok(Map.of("message", "deleted"));
    }
    
}
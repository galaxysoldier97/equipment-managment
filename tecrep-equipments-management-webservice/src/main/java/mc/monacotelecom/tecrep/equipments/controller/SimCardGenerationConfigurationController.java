package mc.monacotelecom.tecrep.equipments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.SimCardGenerationConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v1.AddSimCardGenerationConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchSimCardGenerationConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.UpdateSimCardGenerationConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.service.SimCardGenerationConfigurationService;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "SIM Card Generation Configuration API V1")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping({"private/auth/simGenerationConfigurations", "api/v1/private/auth/simGenerationConfigurations"})
@Deprecated(since = "2.21.0", forRemoval = true)
public class SimCardGenerationConfigurationController {

    private final SimCardGenerationConfigurationService service;

    @Operation(summary = "Search a SIM Card Generation Configuration by several fields", operationId = "searchSCGC")
    @ApiResponse(responseCode = "200", description = "Matching configurations returned")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PageableAsQueryParam
    @Deprecated(since = "2.21.0", forRemoval = true)
    @GetMapping
    public Page<SimCardGenerationConfigurationDTO> search(final SearchSimCardGenerationConfigurationDTO searchSimCardGenerationConfigurationDTO, @Parameter(hidden = true) final Pageable pageable) {
        return service.searchV1(searchSimCardGenerationConfigurationDTO, pageable);
    }

    @Operation(summary = "Get SIM Card generation configuration by name", operationId = "getSimCardGenerationConfigurationByName")
    @ApiResponse(responseCode = "200", description = "SIM Card generation configuration returned")
    @ApiResponse(responseCode = "404", description = "SIM Card generation configuration does not exist")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @Deprecated(since = "2.21.0", forRemoval = true)
    @GetMapping("/{name}")
    public SimCardGenerationConfigurationDTO getByName(final @PathVariable("name") String name) {
        return service.getByNameV1(name);
    }

    @Operation(summary = "Delete SIM Card generation configuration by code", operationId = "deleteSimCardGenerationConfigurationByName")
    @ApiResponse(responseCode = "204", description = "SIM Card generation configuration returned")
    @ApiResponse(responseCode = "400", description = "SIM Card generation configuration cannot be deleted")
    @ApiResponse(responseCode = "404", description = "SIM Card generation configuration does not exist")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @Deprecated(since = "2.21.0", forRemoval = true)
    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByCode(final @PathVariable("name") String name) {
        service.deleteByName(name);
    }

    @Operation(summary = "Create SIM Card generation configuration", operationId = "createSimCardGenerationConfiguration")
    @ApiResponse(responseCode = "201", description = "SIM Card generation configuration created")
    @ApiResponse(responseCode = "400", description = "SIM Card generation configuration cannot be created")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @Deprecated(since = "2.21.0", forRemoval = true)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SimCardGenerationConfigurationDTO create(final @Valid @RequestBody AddSimCardGenerationConfigurationDTO simCardGenerationConfigurationDTO) {
        return service.createV1(simCardGenerationConfigurationDTO);
    }

    @Operation(summary = "Partial update SIM Card generation configuration", operationId = "updateSimCardGenerationConfiguration")
    @ApiResponse(responseCode = "200", description = "SIM Card generation configuration updated")
    @ApiResponse(responseCode = "400", description = "SIM Card generation configuration cannot be updated")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @Deprecated(since = "2.21.0", forRemoval = true)
    @PatchMapping("/{name}")
    public SimCardGenerationConfigurationDTO update(final @Valid @RequestBody UpdateSimCardGenerationConfigurationDTO simCardGenerationConfigurationDTO,
                                                    final @PathVariable("name") String name) {
        return service.updateV1(name, simCardGenerationConfigurationDTO);
    }
}

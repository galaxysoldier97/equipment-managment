package mc.monacotelecom.tecrep.equipments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddSimCardGenerationConfigurationDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchSimCardGenerationConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateSimCardGenerationConfigurationV2DTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.SimCardGenerationConfigurationDTOV2;
import mc.monacotelecom.tecrep.equipments.service.SimCardGenerationConfigurationService;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "SIM Card Generation Configuration API V2")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/private/auth/simGenerationConfigurations")
public class SimCardGenerationConfigurationControllerV2 {

    private final SimCardGenerationConfigurationService service;

    @Operation(summary = "Search a SIM Card Generation Configuration by several fields")
    @ApiResponse(responseCode = "200", description = "Matching configurations returned")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PageableAsQueryParam
    @GetMapping
    public Page<SimCardGenerationConfigurationDTOV2> search(final SearchSimCardGenerationConfigurationDTO searchSimCardGenerationConfigurationDTO, @Parameter(hidden = true) final Pageable pageable) {
        return service.search(searchSimCardGenerationConfigurationDTO, pageable);
    }

    @Operation(summary = "Get SIM Card generation configuration by name")
    @ApiResponse(responseCode = "200", description = "SIM Card generation configuration returned")
    @ApiResponse(responseCode = "404", description = "SIM Card generation configuration does not exist")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @GetMapping("/{name}")
    public SimCardGenerationConfigurationDTOV2 getByName(final @PathVariable("name") String name) {
        return service.getByName(name);
    }

    @Operation(summary = "Delete SIM Card generation configuration by code")
    @ApiResponse(responseCode = "204", description = "SIM Card generation configuration returned")
    @ApiResponse(responseCode = "400", description = "SIM Card generation configuration cannot be deleted")
    @ApiResponse(responseCode = "404", description = "SIM Card generation configuration does not exist")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByCode(final @PathVariable("name") String name) {
        service.deleteByName(name);
    }

    @Operation(summary = "Create SIM Card generation configuration")
    @ApiResponse(responseCode = "201", description = "SIM Card generation configuration created")
    @ApiResponse(responseCode = "400", description = "SIM Card generation configuration cannot be created")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SimCardGenerationConfigurationDTOV2 create(final @Valid @RequestBody AddSimCardGenerationConfigurationDTOV2 dto) {
        return service.create(dto);
    }

    @Operation(summary = "Partial update SIM Card generation configuration")
    @ApiResponse(responseCode = "200", description = "SIM Card generation configuration updated")
    @ApiResponse(responseCode = "400", description = "SIM Card generation configuration cannot be updated")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PatchMapping("/{name}")
    public SimCardGenerationConfigurationDTOV2 update(final @Valid @RequestBody UpdateSimCardGenerationConfigurationV2DTO dto,
                                                      final @PathVariable("name") String name) {
        return service.update(name, dto);
    }
}

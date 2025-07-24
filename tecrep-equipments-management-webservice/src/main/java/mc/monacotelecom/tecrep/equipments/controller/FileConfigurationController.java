package mc.monacotelecom.tecrep.equipments.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.FileConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchFileConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.UpdateFileConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.service.FileConfigurationService;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Tag(name = "File Configuration API")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping({"private/auth/fileConfiguration", "api/v1/private/auth/fileConfiguration"})
public class FileConfigurationController {

    private final FileConfigurationService fileConfigurationService;

    @Operation(summary = "Search File Configurations by several fields", operationId = "searchFileConfigurations")
    @ApiResponse(responseCode = "200", description = "Matching File configurations returned")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PageableAsQueryParam
    @GetMapping
    public Page<FileConfigurationDTO> search(final SearchFileConfigurationDTO searchFileConfigurationDTO, @Parameter(hidden = true) final Pageable pageable) {
        return fileConfigurationService.search(searchFileConfigurationDTO, pageable);
    }

    @Operation(summary = "Get File configuration by name", operationId = "getFileConfigurationByName")
    @ApiResponse(responseCode = "200", description = "File configuration returned")
    @ApiResponse(responseCode = "404", description = "File configuration does not exist")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @GetMapping("/{name}")
    public FileConfigurationDTO getByName(final @PathVariable("name") String name) {
        return fileConfigurationService.getByName(name);
    }

    @Operation(summary = "Delete File configuration by code", operationId = "deleteFileConfigurationByName")
    @ApiResponse(responseCode = "204", description = "File configuration returned")
    @ApiResponse(responseCode = "400", description = "File configuration cannot be deleted")
    @ApiResponse(responseCode = "404", description = "File configuration does not exist")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByCode(final @PathVariable("name") String name) {
        fileConfigurationService.deleteByName(name);
    }

    @Operation(summary = "Create File configuration", operationId = "createFileConfiguration")
    @ApiResponse(responseCode = "201", description = "File configuration created")
    @ApiResponse(responseCode = "400", description = "File configuration cannot be created")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FileConfigurationDTO create(final @Valid @RequestBody FileConfigurationDTO fileConfigurationDTO) {
        return fileConfigurationService.create(fileConfigurationDTO);
    }

    @Operation(summary = "Partial update File configuration", operationId = "updateFileConfiguration")
    @ApiResponse(responseCode = "200", description = "File configuration updated")
    @ApiResponse(responseCode = "400", description = "File configuration cannot be updated")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PatchMapping("/{name}")
    public FileConfigurationDTO update(final @Valid @RequestBody UpdateFileConfigurationDTO fileConfigurationDto,
                                       final @PathVariable("name") String name) {
        return fileConfigurationService.update(name, fileConfigurationDto);
    }
}

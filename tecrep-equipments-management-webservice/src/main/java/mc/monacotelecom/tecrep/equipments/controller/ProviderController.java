package mc.monacotelecom.tecrep.equipments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.ProviderDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchProviderDTO;
import mc.monacotelecom.tecrep.equipments.entity.Provider;
import mc.monacotelecom.tecrep.equipments.service.ProviderService;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Provider API V1")
@CrossOrigin
@RestController
@Deprecated(since = "2.21.0", forRemoval = true)
@RequestMapping({"private/auth/providers", "api/v1/private/auth/providers"})
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    @Operation(summary = "Find a provider by Id", operationId = "getProviderById")
    @ApiResponse(responseCode = "200", description = "Provider found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @Deprecated(since = "2.21.0", forRemoval = true)
    @GetMapping("/{providerId}")
    public ProviderDTO getById(@PathVariable("providerId") Long providerId) {
        return providerService.getByIdV1(providerId);
    }

    @Operation(summary = "Add a provider", operationId = "addProvider")
    @ApiResponse(responseCode = "204", description = "Provider created")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @Deprecated(since = "2.21.0", forRemoval = true)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProviderDTO add(@Valid @RequestBody ProviderDTO providerDTO) {
        return providerService.addV1(providerDTO);
    }

    @Operation(summary = "Update a warehouse", operationId = "updateProvider")
    @ApiResponse(responseCode = "200", description = "Provider updated")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @Deprecated(since = "2.21.0", forRemoval = true)
    @PutMapping("/{providerId}")
    public ProviderDTO update(@PathVariable("providerId") Long providerId, @Valid @RequestBody ProviderDTO providerDTO) {
        return providerService.updateV1(providerId, providerDTO);
    }

    @Operation(summary = "Get all providers", operationId = "getAllProvider")
    @Deprecated(since = "2.21.0", forRemoval = true)
    @GetMapping
    @PageableAsQueryParam
    public PagedModel<ProviderDTO> getAll(@Parameter(hidden = true) Pageable pageable, @Parameter(hidden = true) PagedResourcesAssembler<Provider> assembler) {
        return providerService.getAll(pageable, assembler);
    }

    @Operation(summary = "Delete a provider by id", operationId = "deleteProviderById")
    @Deprecated(since = "2.21.0", forRemoval = true)
    @DeleteMapping("/{providerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("providerId") Long providerId) {
        providerService.delete(providerId);
    }

    @Operation(summary = "Search a provider by code, accesstype,...", operationId = "searchProvider")
    @PageableAsQueryParam
    @Deprecated(since = "2.21.0", forRemoval = true)
    @GetMapping("/search")
    public PagedModel<ProviderDTO> search(SearchProviderDTO searchProviderDTO, @Parameter(hidden = true) Pageable pageable, @Parameter(hidden = true) PagedResourcesAssembler<Provider> assembler) {
        return providerService.searchV1(searchProviderDTO, pageable, assembler);
    }
}

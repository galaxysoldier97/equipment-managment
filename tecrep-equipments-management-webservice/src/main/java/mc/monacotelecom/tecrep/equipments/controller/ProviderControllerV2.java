package mc.monacotelecom.tecrep.equipments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchProviderDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.ProviderDTOV2;
import mc.monacotelecom.tecrep.equipments.service.ProviderService;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Provider API V2")
@CrossOrigin
@RestController
@RequestMapping("api/v2/private/auth/providers")
@RequiredArgsConstructor
public class ProviderControllerV2 {

    private final ProviderService providerService;

    @Operation(summary = "Find a provider by ID")
    @ApiResponse(responseCode = "200", description = "Provider found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @GetMapping("/{providerId}")
    public ProviderDTOV2 getById(@PathVariable("providerId") Long providerId) {
        return providerService.getById(providerId);
    }

    @Operation(summary = "Add a provider")
    @ApiResponse(responseCode = "204", description = "Provider created")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProviderDTOV2 add(@Valid @RequestBody ProviderDTOV2 providerDTO) {
        return providerService.add(providerDTO);
    }

    @Operation(summary = "Update a warehouse")
    @ApiResponse(responseCode = "200", description = "Provider updated")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PutMapping("/{providerId}")
    public ProviderDTOV2 update(@PathVariable("providerId") Long providerId, @Valid @RequestBody ProviderDTOV2 providerDTO) {
        return providerService.update(providerId, providerDTO);
    }

    @Operation(summary = "Delete a provider by id")
    @DeleteMapping("/{providerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("providerId") Long providerId) {
        providerService.delete(providerId);
    }

    @Operation(summary = "Search a provider by several criteria")
    @PageableAsQueryParam
    @GetMapping
    public Page<ProviderDTOV2> search(SearchProviderDTO searchProviderDTO, @Parameter(hidden = true) Pageable pageable) {
        return providerService.search(searchProviderDTO, pageable);
    }
}

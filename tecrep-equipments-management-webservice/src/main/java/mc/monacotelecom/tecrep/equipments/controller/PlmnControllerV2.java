package mc.monacotelecom.tecrep.equipments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchPlmnDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.PlmnDTOV2;
import mc.monacotelecom.tecrep.equipments.service.PlmnService;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "PLMN API V2")
@CrossOrigin
@RestController
@RequestMapping("api/v2/private/auth/plmns")
@RequiredArgsConstructor
public class PlmnControllerV2 {

    private final PlmnService plmnService;

    @Operation(summary = "Find a PLMN by ID")
    @ApiResponse(responseCode = "200", description = "PLMN found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @GetMapping("/{plmnId}")
    public PlmnDTOV2 getById(@PathVariable("plmnId") Long plmnId) {
        return plmnService.getById(plmnId);
    }

    @Operation(summary = "Add a PLMN")
    @ApiResponse(responseCode = "201", description = "PLMN created")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlmnDTOV2 add(@Valid @RequestBody PlmnDTOV2 plmnDTO) {
        return plmnService.add(plmnDTO);
    }

    @Operation(summary = "Update a PLMN")
    @ApiResponse(responseCode = "200", description = "PLMN updated")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PutMapping("/{id}")
    public PlmnDTOV2 update(@PathVariable("id") Long id, @Valid @RequestBody PlmnDTOV2 dto) {
        return plmnService.update(id, dto);
    }

    @Operation(summary = "Delete a PLMN by id")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        plmnService.delete(id);
    }

    @Operation(summary = "Search a PLMN by several criteria")
    @ApiResponse(responseCode = "200", description = "PLMN returned")
    @ApiResponse(responseCode = "400", description = "No PLMN has been found")
    @PageableAsQueryParam
    @GetMapping
    public Page<PlmnDTOV2> search(SearchPlmnDTO dto, @Parameter(hidden = true) Pageable pageable) {
        return plmnService.search(dto, pageable);
    }
}

package mc.monacotelecom.tecrep.equipments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.PlmnDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchPlmnDTO;
import mc.monacotelecom.tecrep.equipments.entity.Plmn;
import mc.monacotelecom.tecrep.equipments.service.PlmnService;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "PLMN API V1")
@CrossOrigin
@RestController
@RequestMapping({"private/auth/plmns", "api/v1/private/auth/plmns"})
@Deprecated(since = "2.21.0", forRemoval = true)
@RequiredArgsConstructor
public class PlmnController {

    private final PlmnService plmnService;

    @Operation(summary = "Find a PLMN by Id", operationId = "getPlmnById")
    @ApiResponse(responseCode = "200", description = "PLMN found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @Deprecated(since = "2.21.0", forRemoval = true)
    @GetMapping("/{plmnId}")
    public PlmnDTO getById(@PathVariable("plmnId") Long plmnId) {
        return plmnService.getByIdV1(plmnId);
    }

    @Operation(summary = "Add a PLMN", operationId = "addPlmn")
    @ApiResponse(responseCode = "201", description = "PLMN created")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @Deprecated(since = "2.21.0", forRemoval = true)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlmnDTO add(@Valid @RequestBody PlmnDTO plmnDTO) {
        return plmnService.addV1(plmnDTO);
    }

    @Operation(summary = "Update a PLMN", operationId = "updatePlmn")
    @ApiResponse(responseCode = "200", description = "PLMN updated")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @Deprecated(since = "2.21.0", forRemoval = true)
    @PutMapping("/{plmnId}")
    public PlmnDTO update(@PathVariable("plmnId") Long plmnId, @Valid @RequestBody PlmnDTO plmnDTO) {
        return plmnService.updateV1(plmnId, plmnDTO);
    }

    @Operation(summary = "Get all plmns", operationId = "getAllPlmns")
    @Deprecated(since = "2.21.0", forRemoval = true)
    @GetMapping
    @PageableAsQueryParam
    public PagedModel<PlmnDTO> getAll(@Parameter(hidden = true) Pageable pageable, @Parameter(hidden = true) PagedResourcesAssembler<Plmn> assembler) {
        return plmnService.getAll(pageable, assembler);
    }

    @Operation(summary = "Delete a PLMN by id", operationId = "deletePlmnById")
    @Deprecated(since = "2.21.0", forRemoval = true)
    @DeleteMapping("/{plmnId}")
    public void deleteById(@PathVariable("plmnId") Long plmnId) {
        plmnService.delete(plmnId);
    }

    @Operation(summary = "Search a PLMN by code,partial of rangesPrefix", operationId = "searchPlmn")
    @ApiResponse(responseCode = "200", description = "PLMN returned")
    @ApiResponse(responseCode = "400", description = "No PLMN has been found")
    @PageableAsQueryParam
    @Deprecated(since = "2.21.0", forRemoval = true)
    @GetMapping("/search")
    public PagedModel<PlmnDTO> search(SearchPlmnDTO searchPlmnDTO, @Parameter(hidden = true) Pageable pageable, @Parameter(hidden = true) PagedResourcesAssembler<Plmn> assembler) {
        return plmnService.searchV1(searchPlmnDTO, pageable, assembler);
    }
}

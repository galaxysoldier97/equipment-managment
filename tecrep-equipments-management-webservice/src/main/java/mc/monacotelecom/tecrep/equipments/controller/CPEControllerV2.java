package mc.monacotelecom.tecrep.equipments.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.OnPutCpe;
import mc.monacotelecom.tecrep.equipments.dto.request.ChangeStatusDto;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddCPEDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchCpeDto;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateCPEDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.CPEDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.RevisionDTOV2;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.projections.CpeUnmProjection;
import mc.monacotelecom.tecrep.equipments.service.ICPEService;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Tag(name = "CPE API V2")
@CrossOrigin
@RestController
@RequestMapping("api/v2/private/auth/cpes")
@RequiredArgsConstructor
public class CPEControllerV2 {

    private final ICPEService cpeService;

    @Operation(summary = "Find a CPE by ID")
    @ApiResponse(responseCode = "200", description = "CPE found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @GetMapping("/{id}")
    public CPEDTOV2 getById(@PathVariable("id") Long id) {
        return cpeService.getById(id);
    }

    @Operation(summary = "Find CPEs by IDs inside a given list of ids")
    @ApiResponse(responseCode = "200", description = "CPEs found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PostMapping("/equipmentIdsIn")
    public Page<CpeUnmProjection> getEquipmentIdsIn(@RequestBody List<Long> ids) {
        return new PageImpl<>(cpeService.getCpeIdsIn(ids));
    }

    @Operation(summary = "Add a CPE")
    @ApiResponse(responseCode = "201", description = "CPE created")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CPEDTOV2 add(@Valid @RequestBody AddCPEDTOV2 dto) {
        return cpeService.add(dto);
    }

    @Operation(summary = "Update a CPE")
    @ApiResponse(responseCode = "200", description = "CPE updated")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PutMapping("/{id}")
    public CPEDTOV2 update(@PathVariable("id") Long id,
                           @Validated({OnPutCpe.class}) @RequestBody UpdateCPEDTOV2 dto) {
        return cpeService.update(id, dto);
    }

    @Operation(summary = "Partial update a CPE")
    @ApiResponse(responseCode = "200", description = "CPE updated")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PatchMapping("/{id}")
    public CPEDTOV2 partialUpdate(@PathVariable("id") Long id,
                                  @Valid @RequestBody UpdateCPEDTOV2 updateCPEDTO) {
        return cpeService.partialUpdate(id, updateCPEDTO);
    }

    @Operation(summary = "Find CPE revisions by internal ID")
    @ApiResponse(responseCode = "200", description = "Revisions page")
    @PageableAsQueryParam
    @GetMapping(value = "/{id}/revisions")
    public Page<RevisionDTOV2<CPEDTOV2>> getCpeRevisionsById(@PathVariable("id") Long id,
                                                             @Parameter(hidden = true) Pageable pageable) {
        return cpeService.findRevisions(id, pageable);
    }

    @Operation(summary = "Delete a CPE by internal ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") Long id, @RequestParam(defaultValue = "false") boolean forced) {
        cpeService.delete(id, forced);
    }

    @Operation(summary = "Search CPEs by several criteria")
    @PageableAsQueryParam
    @GetMapping
    public Page<CPEDTOV2> search(@Valid SearchCpeDto searchCpeDto, @Parameter(hidden = true) Pageable pageable) {
        return cpeService.search(searchCpeDto, pageable);
    }

    @Operation(summary = "Update cpe state")
    @PatchMapping(value = "/{id}/{event}")
    public CPEDTOV2 changeState(@PathVariable("id") Long id,
                                @PathVariable("event") Event event,
                                @Valid @RequestBody Optional<ChangeStatusDto> dto) {
        return cpeService.changeStatus(id, dto.orElse(new ChangeStatusDto()), event);
    }

    @Operation(summary = "Export CPEs as excel file")
    @GetMapping("/export")
    public HttpEntity<ByteArrayResource> export(final SearchCpeDto dto) throws IOException {
        InputStreamResource file = new InputStreamResource(cpeService.export(dto));

        final String filename = "cpes";
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename + ".xlsx");

        return new HttpEntity<>(new ByteArrayResource(file.getInputStream().readAllBytes()), header);
    }
}
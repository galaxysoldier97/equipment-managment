package mc.monacotelecom.tecrep.equipments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.CPEDTO;
import mc.monacotelecom.tecrep.equipments.dto.OnPutCpe;
import mc.monacotelecom.tecrep.equipments.dto.RevisionDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.ChangeStatusDto;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v1.AddCPEDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchCpeDto;
import mc.monacotelecom.tecrep.equipments.dto.request.update.UpdateCPEDTO;
import mc.monacotelecom.tecrep.equipments.entity.CPE;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.projections.CpeUnmProjection;
import mc.monacotelecom.tecrep.equipments.service.ICPEService;
import mc.monacotelecom.tecrep.equipments.service.ICPEServiceV1;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
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

@Tag(name = "CPE API V1")
@CrossOrigin
@RestController
@RequestMapping({"private/auth/cpes", "api/v1/private/auth/cpes"})
@RequiredArgsConstructor
@Deprecated(since = "2.21.0", forRemoval = true)
public class CPEController {

    private final ICPEService cpeService;
    private final ICPEServiceV1 cpeServiceV1;

    @Operation(summary = "Find a CPE by ID", operationId = "getCpeById")
    @ApiResponse(responseCode = "200", description = "CPE found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @GetMapping("/{id}")
    @Deprecated(since = "2.21.0", forRemoval = true)
    public CPEDTO getById(@PathVariable("id") Long cpeId) {
        return cpeServiceV1.getByIdV1(cpeId);
    }

    @Operation(summary = "Find CPEs by IDs inside a given list of ids", operationId = "getIdsIn")
    @ApiResponse(responseCode = "200", description = "CPEs found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PostMapping("/equipmentIdsIn")
    @Deprecated(since = "2.21.0", forRemoval = true)
    public Page<CpeUnmProjection> getEquipmentIdsIn(@RequestBody List<Long> ids) {
        return new PageImpl<>(cpeService.getCpeIdsIn(ids));
    }

    @Operation(summary = "Add a CPE", operationId = "addCpe")
    @ApiResponse(responseCode = "201", description = "CPE created")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public CPEDTO add(@RequestBody AddCPEDTO cpedto) {
        return cpeServiceV1.addV1(cpedto);
    }

    @Operation(summary = "Update a CPE", operationId = "updateCpe")
    @ApiResponse(responseCode = "200", description = "CPE updated")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PutMapping("/{id}")
    @Deprecated(since = "2.21.0", forRemoval = true)
    public CPEDTO update(@PathVariable("id") Long id,
                         @Validated({OnPutCpe.class}) @RequestBody UpdateCPEDTO updateCPEDTO) {
        return cpeServiceV1.updateV1(id, updateCPEDTO);
    }

    @Operation(summary = "Partial update a CPE", operationId = "partialUpdateCpe")
    @ApiResponse(responseCode = "200", description = "CPE updated")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PatchMapping("/{id}")
    @Deprecated(since = "2.21.0", forRemoval = true)
    public CPEDTO partialUpdate(@PathVariable("id") Long id,
                                @Valid @RequestBody UpdateCPEDTO updateCPEDTO) {
        return cpeServiceV1.partialUpdateV1(id, updateCPEDTO);
    }

    @Operation(summary = "Get all cpes", operationId = "getAllCpes")
    @GetMapping
    @Deprecated(since = "2.21.0", forRemoval = true)
    @PageableAsQueryParam
    public PagedModel<CPEDTO> getAll(@Parameter(hidden = true) Pageable pageable, @Parameter(hidden = true) PagedResourcesAssembler<CPE> assembler) {
        return cpeServiceV1.getAllV1(pageable, assembler);
    }

    @Operation(summary = "Find cpe revisions by Id", operationId = "getCpeRevisionsById")
    @ApiResponse(responseCode = "200", description = "Revisions page")
    @GetMapping(value = "/{id}/revisions")
    @PageableAsQueryParam
    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<RevisionDTO<CPEDTO>> getCpeRevisionsById(@PathVariable("id") Long id,
                                                               @Parameter(hidden = true) Pageable pageable,
                                                               @Parameter(hidden = true) PagedResourcesAssembler<Revision<Integer, CPE>> assembler) {
        return cpeServiceV1.findRevisionsV1(id, pageable, assembler);
    }

    @Operation(summary = "Delete a CPE by id", operationId = "deleteCpeById")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public void deleteById(@PathVariable("id") Long id) {
        cpeService.delete(id, false);
    }

    @Operation(summary = "Search a CPE by code,...", operationId = "searchCpe")
    @PageableAsQueryParam
    @GetMapping("/search")
    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<CPEDTO> search(SearchCpeDto searchCpeDto, @Parameter(hidden = true) Pageable pageable, @Parameter(hidden = true) PagedResourcesAssembler<CPE> assembler) {
        return cpeServiceV1.searchV1(searchCpeDto, pageable, assembler);
    }

    @Operation(summary = "Update cpe state : instore, book, ...", operationId = "changeCpeState")
    @PatchMapping(value = "/{id}/{event}")
    @Deprecated(since = "2.21.0", forRemoval = true)
    public CPEDTO changeState(@PathVariable("id") Long id,
                              @PathVariable("event") Event event,
                              @Valid @RequestBody Optional<ChangeStatusDto> changeStateDTO) {
        return cpeServiceV1.changeStatusV1(id, changeStateDTO.orElse(new ChangeStatusDto()), event);
    }

    @Operation(summary = "Export cpe as excel file", operationId = "export")
    @GetMapping("/export")
    @Deprecated(since = "2.21.0", forRemoval = true)
    public HttpEntity<ByteArrayResource> export(final SearchCpeDto searchCpeDto) throws IOException {
        InputStreamResource file = new InputStreamResource(cpeService.export(searchCpeDto));

        final String filename = "cpes";
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename + ".xlsx");

        return new HttpEntity<>(new ByteArrayResource(file.getInputStream().readAllBytes()), header);
    }
}
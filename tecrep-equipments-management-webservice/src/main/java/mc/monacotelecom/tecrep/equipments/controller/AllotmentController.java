package mc.monacotelecom.tecrep.equipments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.dto.AllotmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.AllotmentProvisionedDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.AllotmentExportRequestDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.AllotmentRequestDTO;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.service.AllotmentService;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.ALLOTMENT_ID_NOT_MATCHING;

@Tag(name = "Allotment Summary Management")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping({"private/auth/allotments", "api/v1/private/auth/allotments"})
@RequiredArgsConstructor
public class AllotmentController {

    private final AllotmentService service;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    @Operation(summary = "Get all Allotments")
    @ApiResponse(responseCode = "200", description = "Allotments for the batch found")
    @GetMapping
    @PageableAsQueryParam
    public Page<AllotmentDTO> getAll(@Parameter(hidden = true) Pageable pageable) {
        return service.getAll(pageable);
    }

    @Operation(summary = "Find Allotment by Id")
    @ApiResponse(responseCode = "200", description = "Allotment found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("/{id}")
    public AllotmentDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @Operation(summary = "Find all Allotment in a batch")
    @ApiResponse(responseCode = "200", description = "Allotments for the batch found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("/batch/{batchNumber}")
    @PageableAsQueryParam
    public Page<AllotmentDTO> getByBatchNumber(@PathVariable Long batchNumber, @Parameter(hidden = true) Pageable pageable) {
        return service.getAllByBatchNumber(batchNumber, pageable);
    }

    @Operation(summary = "Add Allotment")
    @ApiResponse(responseCode = "201", description = "Allotment created")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AllotmentDTO add(@Valid @RequestBody AllotmentRequestDTO allotmentDTO) {
        return service.add(allotmentDTO);
    }

    @Operation(summary = "Generate SIM Packaging File")
    @ApiResponse(responseCode = "200", description = "Packaging File returned")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PostMapping("/export")
    public ResponseEntity<ByteArrayResource> export(@Valid @RequestBody AllotmentExportRequestDTO dto) throws IOException {
        var fileContent = service.export(dto).readAllBytes();
        var filename = service.getAllotmentFilename(dto);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", filename);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ByteArrayResource(fileContent));
    }

    @Operation(summary = "Provisioned Allotment")
    @ApiResponse(responseCode = "201", description = "Allotment provisioned")
    @ApiResponse(responseCode = "400", description = "Validation failure, e.g. id or quantity incorrect")
    @ApiResponse(responseCode = "404", description = "Data entity not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PostMapping("/{id}/provisioned")
    @ResponseStatus(HttpStatus.CREATED)
    public AllotmentDTO provisioned(@PathVariable Long id, @Valid @RequestBody AllotmentProvisionedDTO dto) {
        if (!id.equals(dto.getAllotmentId())) {
            throw new EqmValidationException(localizedMessageBuilder, ALLOTMENT_ID_NOT_MATCHING, dto.getAllotmentId(), id);
        }
        return service.provisioned(dto);
    }
}

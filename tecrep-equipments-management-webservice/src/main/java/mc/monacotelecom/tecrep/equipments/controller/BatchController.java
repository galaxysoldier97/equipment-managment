package mc.monacotelecom.tecrep.equipments.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.dto.BatchDTO;
import mc.monacotelecom.tecrep.equipments.dto.GenerateSimCardsResponseDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.CreateBatchRequestDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.GenerateSimCardsRequestDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchBatchDTO;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.service.BatchServiceV1;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.http.entity.ContentType;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.SIMCARD_GENERATION_NOT_ENOUGH_MSISDN;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@Tag(name = "Batch Management")
@Slf4j
@Validated
@CrossOrigin
@RestController
@RequestMapping({"private/auth/batch", "api/v1/private/auth/batch"})
@RequiredArgsConstructor
@Deprecated(since = "2.21.0", forRemoval = true)
public class BatchController {

    private final BatchServiceV1 batchService;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Operation(summary = "Get all batches", operationId = "getAllBatches")
    @GetMapping
    @PageableAsQueryParam
    public Page<BatchDTO> getAll(@Parameter(hidden = true) Pageable pageable) {
        return batchService.getAll(pageable);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Operation(summary = "Get single batch by batch number", operationId = "getBatch")
    @GetMapping("/{batchNumber}")
    public BatchDTO getByBatchNumber(@PathVariable final Long batchNumber) {
        return batchService.getByBatchNumber(batchNumber);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Operation(summary = "Delete batch by batch number, along with its related SIMs", operationId = "deleteBatch")
    @DeleteMapping("/{batchNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByBatchNumber(@PathVariable final Long batchNumber) {
        log.info(String.format("Deleting batch with number '%s'", batchNumber));
        batchService.deleteByBatchNumber(batchNumber);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Operation(summary = "Search batches by several fields", operationId = "searchBatch")
    @PageableAsQueryParam
    @GetMapping("/search")
    public Page<BatchDTO> search(SearchBatchDTO searchBatchDTO, @Parameter(hidden = true) Pageable pageable) {
        return batchService.search(searchBatchDTO, pageable);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Operation(summary = "Create a new batch", operationId = "createBatch")
    @ApiResponse(responseCode = "201", description = "Batch has been successfully created")
    @ApiResponse(responseCode = "500", description = "Internal processing error")
    @PostMapping
    public BatchDTO createBatch(@Valid @RequestBody CreateBatchRequestDTO createBatchRequestDTO) {
        return batchService.createBatch(createBatchRequestDTO);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Operation(summary = "Export a SIM card manufacturing request", operationId = "exportSIMCard")
    @ApiResponse(responseCode = "200", description = "SIM cards exported for manufacturing")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PostMapping("/{batchNumber}/export")
    public GenerateSimCardsResponseDTO generateSimCards(@PathVariable Long batchNumber, @RequestBody @Valid GenerateSimCardsRequestDTO generateSimCardsRequestDTO) {
        if (!generateSimCardsRequestDTO.getNumbers().isEmpty() && generateSimCardsRequestDTO.getNumbers().size() != generateSimCardsRequestDTO.getQuantity()) {
            throw new EqmValidationException(localizedMessageBuilder, SIMCARD_GENERATION_NOT_ENOUGH_MSISDN,
                    generateSimCardsRequestDTO.getQuantity(),
                    generateSimCardsRequestDTO.getNumbers().size());
        }

        return batchService.generateSimCards(batchNumber, generateSimCardsRequestDTO);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Operation(summary = "Set the processed date a a Batch to the current time", operationId = "setProcessedDate")
    @PatchMapping("/{batchNumber}/process")
    public BatchDTO setProcessedDate(@PathVariable Long batchNumber) {
        return batchService.setProcessedDate(batchNumber);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Operation(summary = "Set the returned date a a Batch to the current time", operationId = "setReturnedDate")
    @PatchMapping("/{batchNumber}/return")
    public BatchDTO setReturnedDate(@PathVariable Long batchNumber) {
        return batchService.setReturnedDate(batchNumber);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Operation(summary = "Upload import file on the server, when returned by provider", operationId = "uploadImportFile")
    @ApiResponse(responseCode = "201", description = "Import file has been successfully uploaded on the server")
    @ApiResponse(responseCode = "404", description = "Related batch could not be found for this batch number.")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PostMapping(path = "/{batchNumber}/upload-import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadImportFile(@PathVariable final Long batchNumber, final @RequestPart(value = "file") MultipartFile file) {
        batchService.uploadImportFile(batchNumber, file);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Operation(summary = "Download import file from the server, for a specific batch", operationId = "downloadImportFile")
    @ApiResponse(responseCode = "200", description = "Import file has been successfully downloaded from the server")
    @ApiResponse(responseCode = "404", description = "Related batch could not be found for this batch number, or file not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @GetMapping(value = "/{batchNumber}/download-import")
    public void downloadImportFile(@PathVariable final Long batchNumber, final HttpServletResponse response) throws IOException {
        final BatchDTO batchDTO = batchService.getByBatchNumber(batchNumber);
        final InputStream is = batchService.getImportFile(batchDTO.getImportFileName());

        response.addHeader(CONTENT_DISPOSITION, "attachment;filename=" + batchDTO.getImportFileName());
        response.setContentType(ContentType.TEXT_PLAIN.getMimeType());

        IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();

        is.close();
    }
}

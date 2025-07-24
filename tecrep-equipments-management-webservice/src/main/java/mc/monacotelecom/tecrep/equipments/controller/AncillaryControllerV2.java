package mc.monacotelecom.tecrep.equipments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.ImportResultDTO;
import mc.monacotelecom.tecrep.equipments.dto.OnPutAncillary;
import mc.monacotelecom.tecrep.equipments.dto.request.AncillaryChangeStateDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddAncillaryDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchAncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateAncillaryEquipmentDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.AncillaryEquipmentDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.RevisionDTOV2;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.service.IAncillaryService;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryImportJob;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryImportError;
 
import mc.monacotelecom.tecrep.equipments.process.ancillary.AncillaryImportService;
import org.springframework.web.server.ResponseStatusException;


@Tag(name = "Ancillary Equipment API V2")
@CrossOrigin
@RestController
@RequestMapping("api/v2/private/auth/ancillaryequipments")
@RequiredArgsConstructor
public class AncillaryControllerV2 {

    private final IAncillaryService ancillaryService;

    @Operation(summary = "Get an Ancillary Equipment by internal ID")
    @ApiResponse(responseCode = "200", description = "AncillaryEquipment found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @GetMapping("/{id}")
    public AncillaryEquipmentDTOV2 getById(@PathVariable(value = "id") @Positive final Long id) {
        return ancillaryService.getById(id);
    }

    @Operation(summary = "Get an Ancillary Equipment by serial number")
    @ApiResponse(responseCode = "200", description = "AncillaryEquipment found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @GetMapping("/serialnumber/{serialNumber}")
    public AncillaryEquipmentDTOV2 getBySerialNumber(@PathVariable(value = "serialNumber") @NotEmpty final String serialNumber) {
        return ancillaryService.getBySerialNumber(serialNumber);
    }

    @Operation(summary = "Get an Ancillary Equipment by paired equipment serial number")
    @ApiResponse(responseCode = "200", description = "AncillaryEquipment found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @GetMapping("/pairedEquipment/{serialNumber}")
    public AncillaryEquipmentDTOV2 getByPairedEquipmentSerial(@PathVariable(value = "serialNumber") final String serialNumber) {
        return ancillaryService.getByPairedEquipmentSerial(serialNumber);
    }

    @Operation(summary = "Search an Ancillary Equipment by several criteria")
    @PageableAsQueryParam
    @GetMapping
    public Page<AncillaryEquipmentDTOV2> search(final SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO,
                                                @Parameter(hidden = true) final Pageable pageable) {
        return ancillaryService.search(searchAncillaryEquipmentDTO, pageable);
    }

    @Operation(summary = "Find Ancillary Equipment revisions by internal ID")
    @PageableAsQueryParam
    @ApiResponse(responseCode = "200", description = "Revisions page")
    @GetMapping(value = "/{id}/revisions")
    public Page<RevisionDTOV2<AncillaryEquipmentDTOV2>> getAncillaryEquipmentRevisionsById(@PathVariable("id") final Long id,
                                                                                           @Parameter(hidden = true) final Pageable pageable) {
        return ancillaryService.findRevisions(id, pageable);
    }

    @Operation(summary = "Add an Ancillary Equipment")
    @ApiResponse(responseCode = "201", description = "Ancillary Equipment created")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AncillaryEquipmentDTOV2 add(@Valid @RequestBody final AddAncillaryDTOV2 dto) {
        return ancillaryService.add(dto);
    }

    @Operation(summary = "Update an Ancillary Equipment by internal ID")
    @ApiResponse(responseCode = "200", description = "AncillaryEquipment updated")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PutMapping("/{id}")
    public AncillaryEquipmentDTOV2 update(@PathVariable("id") Long id,
                                          @Validated({OnPutAncillary.class}) @RequestBody final UpdateAncillaryEquipmentDTOV2 dto) {
        return ancillaryService.update(id, dto);
    }

    @Operation(summary = "Partial update of an Ancillary Equipment by internal ID")
    @ApiResponse(responseCode = "200", description = "AncillaryEquipment updated")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PatchMapping("/{id}")
    public AncillaryEquipmentDTOV2 partialUpdate(@PathVariable("id") final Long id,
                                                 @Valid @RequestBody final UpdateAncillaryEquipmentDTOV2 dto) {
        return ancillaryService.partialUpdate(id, dto);
    }

    @Operation(summary = "Change ancillary equipment state by internal ID")
    @PatchMapping(value = "/{id}/{event}")
    public AncillaryEquipmentDTOV2 changeState(@PathVariable("id") final String id,
                                               @PathVariable("event") final Event event,
                                               @Valid @RequestBody final Optional<AncillaryChangeStateDTO> equipmentChangeStateDTO) {

        return ancillaryService.changeState(id, equipmentChangeStateDTO.orElse(new AncillaryChangeStateDTO()), event);
    }

    @Operation(summary = "Delete a AncillaryEquipment by internal ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        ancillaryService.delete(id);
    }

    @Operation(summary = "Export Ancillary Equipments as excel file")
    @GetMapping("export")
    public HttpEntity<ByteArrayResource> exporter(final SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO) throws IOException {
        InputStreamResource file = new InputStreamResource(ancillaryService.export(searchAncillaryEquipmentDTO));

        final String filename = "ancillaryEquipments";
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename + ".xlsx");

        return new HttpEntity<>(new ByteArrayResource(file.getInputStream().readAllBytes()), header);
    }

    @Operation(summary = "Inicia import Ancillary Equipment (asíncrono)")
    @ApiResponse(responseCode = "202", description = "Import process started, devuelve jobId")
    @ApiResponse(responseCode = "400", description = "Invalid file or format")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PostMapping(
        path = "/import",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<String, Object> importAncillaryEquipments(
            @RequestParam("file") MultipartFile file,
            @RequestParam("format") String format,
            @RequestParam(value = "continueOnError", defaultValue = "true") boolean continueOnError
    ) {
   

        ImportResultDTO result = ancillaryService.scheduleImportJob(file, format, continueOnError);
        return Map.of("jobId", result.getJobId(), "status", result.getStatus());
    }


    @Operation(summary = "Consulta el estado de un job de importación")
    @ApiResponse(responseCode = "200", description = "Devuelve estado del job")
    @ApiResponse(responseCode = "404", description = "Job no encontrado")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @GetMapping("/import/status/{jobId}")
    public Map<String, Object> getImportJobStatus(
            @PathVariable("jobId") Long jobId
    ) {
        Optional<AncillaryImportJob> maybe =   ancillaryService.getImportJobById(jobId);  //
        //getImportJobById 
        

             
        if (maybe.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job no encontrado: " + jobId);
        }
        AncillaryImportJob job = maybe.get();
        Map<String, Object> resp = new HashMap<>();
        resp.put("jobId", job.getId());
        resp.put("status", job.getStatus());
        resp.put("totalLines", job.getTotalLines());
        resp.put("successfulLines", job.getSuccessfulLines());
        resp.put("errorCount", job.getErrorCount());
        resp.put("startedAt", job.getStartedAt());
        resp.put("finishedAt", job.getFinishedAt());
        resp.put("resultFilePath", job.getResultFilePath());

        if (job.getStatus() == AncillaryImportJob.JobStatus.FAILED ||
            job.getStatus() == AncillaryImportJob.JobStatus.SUCCESS_WITH_ERRORS) {
            List<AncillaryImportError> errors = ancillaryService.getImportErrorsByJobId(job.getId());
            List<Map<String, Object>> errDetails = errors.stream()
                    .map(e -> {
                        Map<String, Object> m = new HashMap<>();
                        m.put("id", e.getId());
                        m.put("jobId", e.getJob().getId());
                        m.put("lineNumber", e.getLineNumber());
                        m.put("errorMessage", e.getErrorMessage());
                        m.put("originalLine", e.getOriginalLine());
                        return m;
                    })
                    .collect(Collectors.toList());
            resp.put("errorDetails", errDetails);
        }
        return resp;
    }



}

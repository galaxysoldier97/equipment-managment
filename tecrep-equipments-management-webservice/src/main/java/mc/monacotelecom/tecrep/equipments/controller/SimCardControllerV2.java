package mc.monacotelecom.tecrep.equipments.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.importer.ImportParameters;
import mc.monacotelecom.inventory.common.importer.dto.ImportHistoryDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.ChangeStatusDto;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddSimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchSimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateSimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.RevisionDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.SimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.service.ISimCardService;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Tag(name = "SIM Card API V2")
@CrossOrigin
@RestController
@RequestMapping({"api/v2/private/auth/simcards"})
@RequiredArgsConstructor
public class SimCardControllerV2 {

    private final ISimCardService simCardService;
    private final ObjectMapper objectMapper;

    @Operation(summary = "GET a SIM Card by technical ID")
    @ApiResponse(responseCode = "201", description = "SIM Card found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("/{id}")
    public SimCardDTOV2 getById(@PathVariable("id") Long id) {
        return simCardService.getById(id);
    }

    @Operation(summary = "Get a SIM Card by IMSI")
    @ApiResponse(responseCode = "200", description = "SIM Card found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("/imsi/{imsi}")
    public SimCardDTOV2 getByIMSI(@PathVariable("imsi") String imsi) {
        return simCardService.getByIMSI(imsi);
    }

    @Operation(summary = "Get a SIM Card by ICCID")
    @ApiResponse(responseCode = "200", description = "SIM Card found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("/iccid/{iccid}")
    public SimCardDTOV2 getByICCID(@PathVariable("iccid") String iccid) {
        return simCardService.getByICCID(iccid);
    }

    @Operation(summary = "Add a simcard")
    @ApiResponse(responseCode = "201", description = "SIM Card created")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SimCardDTOV2 add(@Valid @RequestBody AddSimCardDTOV2 simCardDto) {
        return simCardService.add(simCardDto);
    }

    @Operation(summary = "Import prepay SIM cards from manufacturing file by import file name")
    @ApiResponse(responseCode = "200", description = "All SIM cards imported from manufacturing successfully")
    @ApiResponse(responseCode = "204", description = "SIM cards imported from manufacturing, partially processed")
    @ApiResponse(responseCode = "500", description = "Internal processing error")
    @PatchMapping("/import/file/{importFileName}")
    public ImportHistoryDTO importFileByName(@Parameter(example = "MMC00123.out") @PathVariable("importFileName") String fileName,
                                             HttpSession httpSession,
                                             final HttpServletRequest httpServletRequest) {
        ImportParameters importParameters = new ImportParameters(httpServletRequest.getParameterMap());
        return simCardService.importFile(fileName, httpSession.getId(), importParameters);
    }

    @Operation(summary = "Import prepay SIM cards from manufacturing file by batch number")
    @ApiResponse(responseCode = "200", description = "All SIM cards imported from manufacturing successfully")
    @ApiResponse(responseCode = "204", description = "SIM cards imported from manufacturing, partially processed")
    @ApiResponse(responseCode = "500", description = "Internal processing error")
    @PatchMapping("/import/batch/{batchNumber}")
    public ImportHistoryDTO importFileByBatch(@PathVariable("batchNumber") Long batchNumber, HttpSession httpSession, final HttpServletRequest httpServletRequest) {
        ImportParameters importParameters = new ImportParameters(httpServletRequest.getParameterMap());
        return simCardService.importFile(batchNumber, httpSession.getId(), importParameters);
    }

    @Operation(summary = "Update a simcard")
    @PutMapping("/{id}")
    public SimCardDTOV2 update(@PathVariable("id") Long id,
                               @RequestBody UpdateSimCardDTOV2 updateSimCardDTO) {
        return simCardService.update(id, updateSimCardDTO);
    }

    @Operation(summary = "Partial update")
    @PatchMapping("/{id}")
    public SimCardDTOV2 partialUpdateById(@PathVariable("id") Long id,
                                          @RequestBody UpdateSimCardDTOV2 updateSimCardDTO) {
        return simCardService.partialUpdateById(id, updateSimCardDTO);
    }

    @Operation(summary = "Partial update by ICCID")
    @PatchMapping("/serialNumber/{iccid}")
    public SimCardDTOV2 partialUpdateByICCID(@PathVariable("iccid") String iccid,
                                             @RequestBody UpdateSimCardDTOV2 updateSimCardDTO) {
        return simCardService.partialUpdateByICCID(iccid, updateSimCardDTO);
    }

    @Operation(summary = "Partial update by IMSI")
    @PatchMapping("/imsi/{imsi}")
    public SimCardDTOV2 partialUpdateByIMSI(@PathVariable("imsi") String imsi,
                                            @RequestBody UpdateSimCardDTOV2 updateSimCardDTO) {
        return simCardService.partialUpdateByIMSI(imsi, updateSimCardDTO);
    }

    @Operation(summary = "Update the pack id on a SIM card")
    @PatchMapping("/{id}/pack")
    public SimCardDTOV2 updatePackById(@PathVariable("id") Long id,
                                       @RequestBody UpdateSimCardDTOV2 updateSimCardDTO) {
        return simCardService.updatePackById(id, updateSimCardDTO);
    }

    @Operation(summary = "Update the pack id on a SIM card")
    @PatchMapping("/pack")
    public SimCardDTOV2 updatePackByIMSI(@RequestBody UpdateSimCardDTOV2 updateSimCardDTO) {
        return simCardService.updatePackByIMSI(updateSimCardDTO);
    }

    @Operation(summary = "Delete a simcard")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        simCardService.delete(id);
    }

    @Operation(summary = "Search SIM cards by several fields")
    @ApiResponse(responseCode = "200", description = "SIM Cards corresponding to the criteria have been returned")
    @ApiResponse(responseCode = "500", description = "Internal processing error")
    @PageableAsQueryParam
    @GetMapping
    public Page<SimCardDTOV2> search(SearchSimCardDTO searchSimCardDTO, @Parameter(hidden = true) Pageable pageable) throws JsonProcessingException {
        log.info("Searching simcards ...{}", objectMapper.writeValueAsString(searchSimCardDTO));
        return simCardService.search(searchSimCardDTO, pageable);
    }

    @Operation(summary = "Update simcard state by internal ID")
    @PatchMapping(value = "/{id}/{event}")
    public SimCardDTOV2 changeStateById(@PathVariable("id") Long id,
                                        @PathVariable("event") Event event,
                                        @Valid @RequestBody(required = false) Optional<ChangeStatusDto> changeStateDTO) {
        return simCardService.changeStateById(id, event, changeStateDTO.orElse(new ChangeStatusDto()));
    }

    @Operation(summary = "Update simcard state by ICCID")
    @PatchMapping(value = "/serialNumber/{iccid}/{event}")
    public SimCardDTOV2 changeStateByICCID(@PathVariable("iccid") String iccid,
                                           @PathVariable("event") Event event,
                                           @Valid @RequestBody(required = false) Optional<ChangeStatusDto> changeStateDTO) {
        return simCardService.changeStateByICCID(iccid, event, changeStateDTO.orElse(new ChangeStatusDto()));
    }

    @Operation(summary = "Update simcard state by IMSI")
    @PatchMapping(value = "/imsi/{imsi}/{event}")
    public SimCardDTOV2 changeStateByImsi(@PathVariable("imsi") String imsi,
                                          @PathVariable("event") Event event,
                                          @Valid @RequestBody(required = false) Optional<ChangeStatusDto> changeStateDTO) {
        return simCardService.changeStateByIMSI(imsi, event, changeStateDTO.orElse(new ChangeStatusDto()));
    }

    @Operation(summary = "Find available events for a state")
    @GetMapping("/{id}/events")
    public Collection<Event> findEventsForState(@PathVariable("id") Long id) {
        return simCardService.getAvailableEvents(id);
    }

    @Operation(summary = "Find SIM card revisions by equipment ID")
    @ApiResponse(responseCode = "200", description = "Revisions page")
    @PageableAsQueryParam
    @GetMapping(value = "/{id}/revisions")
    public Page<RevisionDTOV2<SimCardDTOV2>> getSimCardRevisionsById(@PathVariable("id") Long id, @Parameter(hidden = true) Pageable pageable) {
        return simCardService.findRevisions(id, pageable);
    }

    @Operation(summary = "Export of SIM cards as excel file")
    @ApiResponse(responseCode = "200", description = "SIMCards corresponding to the criteria have been returned")
    @ApiResponse(responseCode = "500", description = "Internal processing error")
    @GetMapping("/export")
    public HttpEntity<ByteArrayResource> export(final SearchSimCardDTO searchSimCardDTO) throws IOException {

        InputStreamResource file = new InputStreamResource(simCardService.export(searchSimCardDTO));

        final String filename = "simcards";
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename + ".xlsx");

        return new HttpEntity<>(new ByteArrayResource(file.getInputStream().readAllBytes()), header);
    }
}

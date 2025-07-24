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
import mc.monacotelecom.tecrep.equipments.dto.RevisionDTO;
import mc.monacotelecom.tecrep.equipments.dto.SimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.ChangeStatusDto;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v1.AddSimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchSimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.UpdateSimCardDTO;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.service.ISimCardService;
import mc.monacotelecom.tecrep.equipments.service.ISimCardServiceV1;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
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
@Tag(name = "SIM Card API V1")
@CrossOrigin
@RestController
@RequestMapping({"private/auth/simcards", "api/v1/private/auth/simcards"})
@RequiredArgsConstructor
@Deprecated(since = "2.21.0", forRemoval = true)
public class SimCardController {

    private final ISimCardServiceV1 simCardServicev1;
    private final ISimCardService simCardService;
    private final ObjectMapper objectMapper;

    @Operation(summary = "GET a SIM Card by technical ID")
    @ApiResponse(responseCode = "201", description = "SIM Card found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO getById(@PathVariable("id") Long id) {
        return simCardServicev1.getByIdV1(id);
    }

    @Operation(summary = "Get a SIM Card by IMSI")
    @ApiResponse(responseCode = "200", description = "SIM Card found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("/imsi/{imsi}")
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO getByIMSI(@PathVariable("imsi") String imsi) {
        return simCardServicev1.getByIMSIV1(imsi);
    }

    @Operation(summary = "Get a SIM Card by ICCID")
    @ApiResponse(responseCode = "200", description = "SIM Card found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("/iccid/{iccid}")
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO getByICCID(@PathVariable("iccid") String iccid) {
        return simCardServicev1.getByICCIDV1(iccid);
    }

    @Operation(summary = "Add a simcard")
    @ApiResponse(responseCode = "201", description = "SIM Card created")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO add(@Valid @RequestBody AddSimCardDTO dto) {
        return simCardServicev1.addV1(dto);
    }

    @Operation(summary = "Import prepay SIM cards from manufacturing file by import file name")
    @ApiResponse(responseCode = "200", description = "All SIM cards imported from manufacturing successfully")
    @ApiResponse(responseCode = "204", description = "SIM cards imported from manufacturing, partially processed")
    @ApiResponse(responseCode = "500", description = "Internal processing error")
    @PatchMapping("/import/file/{importFileName}")
    @Deprecated(since = "2.21.0", forRemoval = true)
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
    @Deprecated(since = "2.21.0", forRemoval = true)
    public ImportHistoryDTO importFileByBatch(@PathVariable("batchNumber") Long batchNumber, HttpSession httpSession, final HttpServletRequest httpServletRequest) {
        ImportParameters importParameters = new ImportParameters(httpServletRequest.getParameterMap());
        return simCardService.importFile(batchNumber, httpSession.getId(), importParameters);
    }

    @Operation(summary = "Update a simcard")
    @PutMapping("/{id}")
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO update(@PathVariable("id") Long id,
                             @RequestBody UpdateSimCardDTO updateSimCardDTO) {
        return simCardServicev1.updateV1(id, updateSimCardDTO);
    }

    @Operation(summary = "Partial update")
    @PatchMapping("/{id}")
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO partialUpdateById(@PathVariable("id") Long id,
                                        @RequestBody UpdateSimCardDTO updateSimCardDTO) {
        return simCardServicev1.partialUpdateByIdV1(id, updateSimCardDTO);
    }

    @Operation(summary = "Partial update by ICCID")
    @PatchMapping("/serialNumber/{iccid}")
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO partialUpdateByICCID(@PathVariable("iccid") String iccid,
                                           @RequestBody UpdateSimCardDTO updateSimCardDTO) {
        return simCardServicev1.partialUpdateByICCIDV1(iccid, updateSimCardDTO);
    }

    @Operation(summary = "Partial update by IMSI")
    @PatchMapping("/imsi/{imsi}")
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO partialUpdateByIMSI(@PathVariable("imsi") String imsi,
                                          @RequestBody UpdateSimCardDTO updateSimCardDTO) {
        return simCardServicev1.partialUpdateByIMSIV1(imsi, updateSimCardDTO);
    }

    @Operation(summary = "Update the pack id on a SIM card")
    @PatchMapping("/{id}/pack")
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO updatePackById(@PathVariable("id") Long id,
                                     @RequestBody UpdateSimCardDTO updateSimCardDTO) {
        return simCardServicev1.updatePackByIdV1(id, updateSimCardDTO);
    }

    @Operation(summary = "Update the pack id on a SIM card")
    @PatchMapping("/pack")
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO updatePackByIMSI(@RequestBody UpdateSimCardDTO updateSimCardDTO) {
        return simCardServicev1.updatePackByIMSIV1(updateSimCardDTO);
    }

    @Operation(summary = "Delete a simcard")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public void delete(@PathVariable("id") Long id) {
        simCardService.delete(id);
    }

    @Operation(summary = "Search a simcard by several fields")
    @ApiResponse(responseCode = "200", description = "SIMCards corresponding to the criteria have been returned")
    @ApiResponse(responseCode = "500", description = "Internal processing error")
    @PageableAsQueryParam
    @GetMapping("/search")
    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<SimCardDTO> search(SearchSimCardDTO searchSimCardDTO, @Parameter(hidden = true) Pageable pageable, @Parameter(hidden = true) PagedResourcesAssembler<SimCard> assembler) throws JsonProcessingException {
        log.info("Searching simcards ...{}", objectMapper.writeValueAsString(searchSimCardDTO));
        return simCardServicev1.searchV1(searchSimCardDTO, pageable, assembler);
    }

    @Operation(summary = "Get all simcards")
    @GetMapping
    @Deprecated(since = "2.21.0", forRemoval = true)
    @PageableAsQueryParam
    public PagedModel<SimCardDTO> getAll(@Parameter(hidden = true) Pageable pageable, @Parameter(hidden = true) PagedResourcesAssembler<SimCard> assembler) {
        return simCardServicev1.getAllV1(pageable, assembler);
    }

    @Operation(summary = "Update simcard state by internal ID")
    @PatchMapping(value = "/{id}/{event}")
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO changeStateById(@PathVariable("id") Long id,
                                      @PathVariable("event") Event event,
                                      @Valid @RequestBody(required = false) Optional<ChangeStatusDto> changeStateDTO) {
        return simCardServicev1.changeStateByIdV1(id, event, changeStateDTO.orElse(new ChangeStatusDto()));
    }

    @Operation(summary = "Update simcard state by ICCID")
    @PatchMapping(value = "/serialNumber/{iccid}/{event}")
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO changeStateByICCID(@PathVariable("iccid") String iccid,
                                         @PathVariable("event") Event event,
                                         @Valid @RequestBody(required = false) Optional<ChangeStatusDto> changeStateDTO) {
        return simCardServicev1.changeStateByICCIDV1(iccid, event, changeStateDTO.orElse(new ChangeStatusDto()));
    }

    @Operation(summary = "Update simcard state by IMSI")
    @PatchMapping(value = "/imsi/{imsi}/{event}")
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO changeStateByImsi(@PathVariable("imsi") String imsi,
                                        @PathVariable("event") Event event,
                                        @Valid @RequestBody(required = false) Optional<ChangeStatusDto> changeStateDTO) {
        return simCardServicev1.changeStateByIMSIV1(imsi, event, changeStateDTO.orElse(new ChangeStatusDto()));
    }

    @Operation(summary = "Find available events for a state")
    @GetMapping("/{id}/events")
    @Deprecated(since = "2.21.0", forRemoval = true)
    public Collection<Event> findEventsForState(@PathVariable("id") Long id) {
        return simCardService.getAvailableEvents(id);
    }

    @Operation(summary = "Find simcard revisions by equipment Id")
    @ApiResponse(responseCode = "200", description = "Revisions page")
    @GetMapping(value = "/{id}/revisions")
    @PageableAsQueryParam
    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<RevisionDTO<SimCardDTO>> getSimCardRevisionsById(@PathVariable("id") Long id,
                                                                       @Parameter(hidden = true) Pageable pageable,
                                                                       @Parameter(hidden = true) PagedResourcesAssembler<Revision<Integer, SimCard>> assembler) {
        return simCardServicev1.findRevisionsV1(id, pageable, assembler);
    }

    @Operation(summary = "Export of simcard as excel file")
    @ApiResponse(responseCode = "200", description = "SIMCards corresponding to the criteria have been returned")
    @ApiResponse(responseCode = "500", description = "Internal processing error")
    @GetMapping("/export")
    @Deprecated(since = "2.21.0", forRemoval = true)
    public HttpEntity<ByteArrayResource> export(final SearchSimCardDTO searchSimCardDTO) throws IOException {

        InputStreamResource file = new InputStreamResource(simCardService.export(searchSimCardDTO));

        final String filename = "simcards";
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename + ".xlsx");

        return new HttpEntity<>(new ByteArrayResource(file.getInputStream().readAllBytes()), header);
    }
}

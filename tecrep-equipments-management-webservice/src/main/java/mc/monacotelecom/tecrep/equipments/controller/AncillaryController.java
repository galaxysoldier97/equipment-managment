package mc.monacotelecom.tecrep.equipments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.*;
import mc.monacotelecom.tecrep.equipments.dto.request.*;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.service.IAncillaryService;
import mc.monacotelecom.tecrep.equipments.service.IAncillaryServiceV1;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//SearchAncillaryEquipmentDTO
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchAncillaryEquipmentDTO;
//AddAncillaryDTO
import mc.monacotelecom.tecrep.equipments.dto.request.create.v1.AddAncillaryDTO;

import mc.monacotelecom.tecrep.equipments.dto.request.update.UpdateAncillaryEquipmentDTO;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.util.Optional;

@Tag(name = "Ancillary Equipment API V1")
@CrossOrigin
@RestController
@RequestMapping({"private/auth/ancillaryequipments", "api/v1/private/auth/ancillaryequipments"})
@RequiredArgsConstructor
@Deprecated(since = "2.21.0", forRemoval = true)
public class AncillaryController {

    private final IAncillaryServiceV1 ancillaryServiceV1;
    private final IAncillaryService ancillaryService;

    @Operation(summary = "Get an Ancillary Equipment by internal ID")
    @GetMapping("/{id}")
    public AncillaryEquipmentDTO getById(@PathVariable @Positive Long id) {
        return ancillaryServiceV1.getById(id);
    }

    @Operation(summary = "Get an Ancillary Equipment by serial number")
    @GetMapping("/serialnumber/{serialNumber}")
    public AncillaryEquipmentDTO getBySerialNumber(@PathVariable @NotEmpty String serialNumber) {
        return ancillaryServiceV1.getBySerialNumber(serialNumber);
    }

    @Operation(summary = "Get an Ancillary Equipment by paired equipment serial number")
    @GetMapping("/pairedEquipment/{serialNumber}")
    public AncillaryEquipmentDTO getByPairedEquipmentSerial(@PathVariable String serialNumber) {
        return ancillaryServiceV1.getByPairedEquipmentSerial(serialNumber);
    }

    @Operation(summary = "Get all Ancillary Equipments")
    @GetMapping
    @PageableAsQueryParam
    public PagedModel<AncillaryEquipmentDTO> getAll(@Parameter(hidden = true) Pageable pageable,
                                                    @Parameter(hidden = true) PagedResourcesAssembler<AncillaryEquipment> assembler) {
        return ancillaryServiceV1.getAll(pageable, assembler);
    }

    @Operation(summary = "Search an Ancillary Equipment by several criteria")
    @GetMapping("/search")
    @PageableAsQueryParam
    public PagedModel<AncillaryEquipmentDTO> search(SearchAncillaryEquipmentDTO searchDTO,
                                                    @Parameter(hidden = true) Pageable pageable,
                                                    @Parameter(hidden = true) PagedResourcesAssembler<AncillaryEquipment> assembler) {
        return ancillaryServiceV1.search(searchDTO, pageable, assembler);
    }

    @Operation(summary = "Find Ancillary Equipment revisions by internal ID")
    @GetMapping("/{id}/revisions")
    @PageableAsQueryParam
    public PagedModel<RevisionDTO<AncillaryEquipmentDTO>> getRevisions(@PathVariable Long id,
                                                                       @Parameter(hidden = true) Pageable pageable,
                                                                       @Parameter(hidden = true) PagedResourcesAssembler<Revision<Integer, AncillaryEquipment>> assembler) {
        return ancillaryServiceV1.findRevisions(id, pageable, assembler);
    }

    @Operation(summary = "Add an Ancillary Equipment")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AncillaryEquipmentDTO add(@RequestBody AddAncillaryDTO dto) {
        return ancillaryServiceV1.add(dto);
    }

    @Operation(summary = "Update an Ancillary Equipment by internal ID")
    @PutMapping("/{id}")
    public AncillaryEquipmentDTO update(@PathVariable Long id,
                                        @Validated(OnPutAncillary.class) @RequestBody UpdateAncillaryEquipmentDTO dto) {
        return ancillaryServiceV1.update(id, dto);
    }

    @Operation(summary = "Partial update of an Ancillary Equipment by internal ID")
    @PatchMapping("/{id}")
    public AncillaryEquipmentDTO partialUpdate(@PathVariable Long id,
                                               @Valid @RequestBody UpdateAncillaryEquipmentDTO dto) {
        return ancillaryServiceV1.partialUpdate(id, dto);
    }

    @Operation(summary = "Update ancillary equipment state by internal ID")
    @PatchMapping("/{id}/{event}")
    public AncillaryEquipmentDTO changeState(@PathVariable String id,
                                             @PathVariable Event event,
                                             @Valid @RequestBody Optional<AncillaryChangeStateDTO> changeDTO) {
        return ancillaryServiceV1.changeState(id, changeDTO.orElse(new AncillaryChangeStateDTO()), event);
    }

    @Operation(summary = "Delete an Ancillary Equipment by internal ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        ancillaryService.delete(id);
    }

    @Operation(summary = "Export Ancillary Equipments as excel file")
    @GetMapping("/export")
    public ResponseEntity<ByteArrayResource> export(SearchAncillaryEquipmentDTO searchDTO) throws IOException {
        var content = ancillaryService.export(searchDTO).readAllBytes();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "ancillaryEquipments.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ByteArrayResource(content));
    }
}

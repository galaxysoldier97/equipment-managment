package mc.monacotelecom.tecrep.equipments.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.tecrep.equipments.dto.SequenceDTO;
import mc.monacotelecom.tecrep.equipments.dto.SequenceResponseDTO;
import mc.monacotelecom.tecrep.equipments.enums.SequenceType;
import mc.monacotelecom.tecrep.equipments.service.SequenceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Tag(name = "Sequence Management")
@CrossOrigin
@RestController
@RequestMapping("api/v1/private/auth/sequences")
@RequiredArgsConstructor
public class SequenceController {

    private final SequenceService sequenceService;


    @Operation(summary = "Get all sequences by type", operationId = "getAllSequencesByType")
    @ApiResponse(responseCode = "200", description = "Sequences returned")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @GetMapping("/{type}")
    public List<SequenceResponseDTO> getAll(@PathVariable("type") SequenceType sequenceType) {

        return sequenceService.getSequences(sequenceType);
    }

    @Operation(summary = "Add a new sequence for MSIN or ICCID", operationId = "addSequence")
    @ApiResponse(responseCode = "201", description = "Sequence created")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PostMapping("/{type}")
    @ResponseStatus(HttpStatus.CREATED)
    public SequenceDTO add(@PathVariable("type") SequenceType sequenceType, @Valid @RequestBody SequenceDTO sequenceDTO) {
        return sequenceService.addSequence(sequenceType, sequenceDTO);
    }


    @Operation(summary = "Delete sequence MSIN or ICCID by its type and value", operationId = "deleteSequence")
    @ApiResponse(responseCode = "400", description = "Sequence cannot be deleted")
    @ApiResponse(responseCode = "404", description = "Sequence does not exist")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{type}")
    public void deleteSequence(@PathVariable("type") SequenceType sequenceType, @Valid @RequestBody SequenceDTO sequenceDTO) {
        sequenceService.delete(sequenceType, sequenceDTO);
    }
}

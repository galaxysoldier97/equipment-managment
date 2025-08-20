package mc.monacotelecom.tecrep.equipments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.v2.StandardLoadDTOV2;
import mc.monacotelecom.tecrep.equipments.service.StandardLoadService;
import org.springdoc.core.annotations.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Standard Load API")
@CrossOrigin
@RestController
@RequestMapping("api/v2/private/auth/standard-loads")
@RequiredArgsConstructor
public class StandardLoadController {

    private final StandardLoadService standardLoadService;

    @Operation(summary = "Get all standard loads")
    @ApiResponse(responseCode = "200", description = "Standard loads found")
    @PageableAsQueryParam
    @GetMapping
    public Page<StandardLoadDTOV2> getAll(@Parameter(hidden = true) Pageable pageable) {
        return standardLoadService.getAll(pageable);
    }

    @Operation(summary = "Find a standard load by ID")
    @ApiResponse(responseCode = "200", description = "Standard load found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("/{id}")
    public StandardLoadDTOV2 getById(@PathVariable("id") Long id) {
        return standardLoadService.getById(id);
    }

    @Operation(summary = "Add a standard load")
    @ApiResponse(responseCode = "201", description = "Standard load created")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StandardLoadDTOV2 add(@Valid @RequestBody StandardLoadDTOV2 dto) {
        return standardLoadService.add(dto);
    }

    @Operation(summary = "Update a standard load")
    @ApiResponse(responseCode = "200", description = "Standard load updated")
    @PutMapping("/{id}")
    public StandardLoadDTOV2 update(@PathVariable("id") Long id, @Valid @RequestBody StandardLoadDTOV2 dto) {
        return standardLoadService.update(id, dto);
    }
}


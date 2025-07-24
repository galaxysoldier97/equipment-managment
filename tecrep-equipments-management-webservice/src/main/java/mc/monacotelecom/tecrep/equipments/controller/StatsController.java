package mc.monacotelecom.tecrep.equipments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.EquipmentsStatsDto;
import mc.monacotelecom.tecrep.equipments.service.StatsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@Tag(name = "Statistics API")
@RequestMapping({"private/auth/equipmentsDashboard", "api/v1/private/auth/equipmentsDashboard"})
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @Operation(summary = "Collect statistics for the equipments dashboard", operationId = "equipmentsDashboard")
    @ApiResponse(responseCode = "201", description = "Statistics collected")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @GetMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EquipmentsStatsDto getDashboard() {
        return statsService.getDashboard();
    }
}

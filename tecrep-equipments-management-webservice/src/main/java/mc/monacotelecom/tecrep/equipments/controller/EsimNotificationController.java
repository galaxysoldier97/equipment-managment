package mc.monacotelecom.tecrep.equipments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.ESimNotificationDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchEsimNotificationDTO;
import mc.monacotelecom.tecrep.equipments.service.EsimNotificationService;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Esim Notification")
@CrossOrigin
@RestController
@RequestMapping("/esim/notification")
@RequiredArgsConstructor
public class EsimNotificationController {

    private final EsimNotificationService esimNotificationService;

    @Operation(summary = "Search Esim notification by several criteria")
    @ApiResponse(responseCode = "200", description = "Esim notification retrieved")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PageableAsQueryParam
    @GetMapping
    public Page<ESimNotificationDTO> search(@Valid SearchEsimNotificationDTO searchEsimNotificationDTO, @Parameter(hidden = true) Pageable pageable) {
        return esimNotificationService.search(searchEsimNotificationDTO, pageable);
    }
}

package mc.monacotelecom.tecrep.equipments.controller;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.StandardLoadItemsResponse;
import mc.monacotelecom.tecrep.equipments.service.StandardLoadQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StandardLoadQueryController {

    private final StandardLoadQueryService service;

    @GetMapping("/api/v1/standard-load/{idStandard}/items")
    public StandardLoadItemsResponse getItems(@PathVariable Long idStandard) {
        return service.getItemsByStandard(idStandard);
    }
}

package mc.monacotelecom.tecrep.equipments.controller;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.BulkStandardLoadItemsRequest;
import mc.monacotelecom.tecrep.equipments.dto.BulkStandardLoadItemsResponse;
import mc.monacotelecom.tecrep.equipments.service.StandardLoadItemBulkService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/standard-load/items")
public class StandardLoadItemController {

    private final StandardLoadItemBulkService service;

    @PostMapping
    public BulkStandardLoadItemsResponse upsert(@RequestBody BulkStandardLoadItemsRequest request) {
        return service.upsertItems(request);
    }
}

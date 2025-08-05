package mc.monacotelecom.tecrep.equipments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.v2.PoAncillaryEquipmentSapDTOV2;
import mc.monacotelecom.tecrep.equipments.service.PoAncillaryEquipmentSapService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Tag(name = "Po Ancillary Equipment SAP API")
@CrossOrigin
@RestController
@RequestMapping("api/v2/private/auth/poAncillaryEquipmentSap")
@RequiredArgsConstructor
public class PoAncillaryEquipmentSapController {

    private final PoAncillaryEquipmentSapService service;

    @Operation(summary = "Get all PO records")
    @GetMapping
    public List<PoAncillaryEquipmentSapDTOV2> getAll() {
        return service.getAll();
    }
}
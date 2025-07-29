package mc.monacotelecom.tecrep.equipments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.entity.HomologacionMaterialSap;
import mc.monacotelecom.tecrep.equipments.service.HomologacionMaterialSapService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Homologacion Material SAP API")
@CrossOrigin
@RestController
@RequestMapping("api/v2/private/auth/homologacionMaterialSap")
@RequiredArgsConstructor
public class HomologacionMaterialSapController {

    private final HomologacionMaterialSapService service;

    @Operation(summary = "Get all homologacion material records")
    @GetMapping
    public List<HomologacionMaterialSap> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Add a homologacion material record")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HomologacionMaterialSap add(@RequestBody HomologacionMaterialSap homologacionMaterialSap) {
        return service.add(homologacionMaterialSap);
    }

    @Operation(summary = "Update a homologacion material record")
    @PatchMapping
    public HomologacionMaterialSap update(@RequestBody HomologacionMaterialSap homologacionMaterialSap) {
        return service.update(homologacionMaterialSap);
    }
}

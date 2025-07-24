package mc.monacotelecom.tecrep.equipments.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.Activity;
import mc.monacotelecom.tecrep.equipments.enums.AllotmentType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@CrossOrigin
@RestController
@RequestMapping({"public/enums", "api/v1/public/enums"})
public class EnumController {

    @Operation(summary = "Get Access Type values", operationId = "getAccessTypes")
    @GetMapping("/accesstype")
    public List<AccessType> getAccessTypes() {
        return List.of(AccessType.values());
    }

    @Operation(summary = "Get Activity values", operationId = "getActivities")
    @GetMapping("/activity")
    public List<Activity> getActivities() {
        return List.of(Activity.values());
    }

    @Operation(summary = "Get Allotment types", operationId = "getAllotmentTypes")
    @GetMapping("/allotmenttype")
    public List<AllotmentType> getAllotmentTypes() {
        return List.of(AllotmentType.values());
    }
}

package mc.monacotelecom.tecrep.equipments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.service.EquipmentTempService;
import mc.monacotelecom.tecrep.equipments.dto.SendValidEquipmentsResponseDTO;
import mc.monacotelecom.tecrep.equipments.dto.ImportResultDTO;
import mc.monacotelecom.tecrep.equipments.entity.EquipmentTemp;
import mc.monacotelecom.tecrep.equipments.dto.UploadEquipmentTempResponseDTO;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "EquipmentTemp API")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping({"private/auth/equipmentstemp", "api/v1/private/auth/equipmentstemp"})
public class EquipmentTempController {

    private final EquipmentTempService equipmentTempService;

    @Operation(summary = "Upload equipments temporary file", operationId = "uploadEquipmentTemp")
    @ApiResponse(responseCode = "201", description = "File processed")
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UploadEquipmentTempResponseDTO upload(@RequestPart("file") MultipartFile file,
                       @RequestParam("model_id") Long modelId,
                       @RequestParam("email") String email,
                       HttpSession httpSession) {
        Long orderUploadId = equipmentTempService.upload(file, modelId, email, httpSession.getId());
        return new UploadEquipmentTempResponseDTO(orderUploadId);
    }


    @Operation(summary = "Get equipment temp by Box SN", operationId = "getEquipmentTempByBoxSn")
    @ApiResponse(responseCode = "200", description = "EquipmentTemp found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("/{boxSn}")
    public EquipmentTemp getByBoxSn(@PathVariable String boxSn) {
        return equipmentTempService.getByBoxSn(boxSn);
    }

    @Operation(summary = "Reset equipment temp scan", operationId = "unscanEquipmentTemp")
    @ApiResponse(responseCode = "200", description = "EquipmentTemp updated")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PatchMapping("/{boxSn}/unscan")
    public EquipmentTemp unscan(@PathVariable String boxSn) {
        return equipmentTempService.unscanByBoxSn(boxSn);
    }

    @Operation(summary = "Get pending equipments by order upload id", operationId = "getEquipmentTempPendingByOrderUploadId")
    @ApiResponse(responseCode = "200", description = "EquipmentsTemp found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("/pending/{orderUploadId}")
    public List<EquipmentTemp> getPendingByOrderUploadId(@PathVariable Long orderUploadId) {
        return equipmentTempService.getPendingByOrderUploadId(orderUploadId);
    }

    @Operation(summary = "Get pending equipments by uploaded email", operationId = "getEquipmentTempPendingByEmail")
    @ApiResponse(responseCode = "200", description = "EquipmentsTemp found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("/pending")
    public List<EquipmentTemp> getPendingByEmail(@RequestParam String email) {
        return equipmentTempService.getPendingByEmail(email);
    }

    @Operation(summary = "Get completed equipments by uploaded email", operationId = "getEquipmentTempCompletedByEmail")
    @ApiResponse(responseCode = "200", description = "EquipmentsTemp found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("/completed")
    public List<EquipmentTemp> getCompletedByEmail(@RequestParam String email) {
        return equipmentTempService.getCompletedByEmail(email);
    }

    @Operation(summary = "Send validated equipments and import", operationId = "sendValidEquipments")
    @ApiResponse(responseCode = "202", description = "Import started")
    @PostMapping("/send-valid-equipments")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SendValidEquipmentsResponseDTO sendValidEquipments(@RequestBody List<Long> ids) {
        return equipmentTempService.sendValidEquipments(ids);
    }
    
}
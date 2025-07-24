package mc.monacotelecom.tecrep.equipments.dto.request.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentName;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentNature;
import mc.monacotelecom.tecrep.equipments.enums.Status;

@Data
public class SearchAncillaryEquipmentDTO {

    @Schema(example = "GFAB02600017")
    private String serialNumber;

    @Schema(description = "Equipment nature")
    private EquipmentNature nature;

    @Schema(description = "If the ancillary equipment is independent")
    private Boolean independent;

    @Schema(example = "6C:99:61:00:02:01")
    private String macAddress;

    @Schema(example = "Wise-10G-Fibre")
    private String modelName;

    @Schema(example = "ONT")
    private EquipmentName equipmentName;

    @Schema(description = "If the ancillary is paired to an equipment")
    private Boolean equipment;

    @Schema(description = "Internal ID of the related paired equipment", example = "1")
    private Long pairedEquipmentId;

    private Status status;

    @Schema(description = "Name of the related provider", example = "Sagemcom")
    private String provider;

    @Schema(description = "Name of the related warehouse", example = "Monaco Telecom Entreprise")
    private String warehouse;

    private AccessType accessType;

    @Schema(description = "If the ancillary equipment has a given sfpVersion")
    private String sfpVersion;

    @Schema(description = "If the ancillary equipment has a service ID", example = "0")
    private Long serviceId;

    @Schema(description = "Search by externla number", example = "7612200000000")
    private String externalNumber;

    @Schema(description = "Batch number", example = "1")
    private String batchNumber;

    @Schema(description = "Order ID", example = "0VWG0R4N")
    private String orderId;

    @Schema(description = "If the ancillary equipment has an order ID")
    private Boolean order;
}

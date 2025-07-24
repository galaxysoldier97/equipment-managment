package mc.monacotelecom.tecrep.equipments.dto.request.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentNature;
import mc.monacotelecom.tecrep.equipments.enums.Status;

@Data
public class SearchCpeDto {

    @Schema(example = "GFAB02600017")
    private String serialNumber;

    @Schema(example = "7C:99:61:00:02:E1")
    private String macAddressCpe;

    @Schema(example = "7C:99:61:00:02:E0")
    private String macAddressRouter;

    @Schema(example = "7C:99:61:00:02:E3")
    private String macAddressVoip;

    @Schema(example = "7C:99:61:00:02:E3", description = "can be a macAddressCpe, macAddressRouter or macAddressVoip")
    private String macAddress;

    @Schema(example = "Fibre Box X6 Black")
    private String modelName;

    private Status status;

    private EquipmentNature nature;

    @Schema(description = "Name of the related provider", example = "Sagemcom")
    private String provider;

    @Schema(description = "Name of the related warehouse", example = "Monaco Telecom Entreprise")
    private String warehouse;

    private AccessType accessType;

    @Schema(example = "7612205998872")
    private String externalNumber;

    @Schema(example = "7C:99:61:00:02:F1")
    private String macAddressLan;

    @Schema(example = "7C:99:61:00:02:F2")
    private String macAddress5G;

    @Schema(example = "7C:99:61:00:02:F1")
    private String macAddress4G;

    @Schema(example = "XGSR1647")
    private String hwVersion;

    @Schema(description = "Batch number", example = "1")
    private String batchNumber;

    @Schema(description = "Order ID", example = "2")
    private String orderId;

    @Schema(description = "If the CPE has an orderId")
    private Boolean order;

    @Schema(description = "Service ID", example = "3")
    private String serviceId;

    @Schema(description = "If the CPE has a serviceId")
    private Boolean service;
}

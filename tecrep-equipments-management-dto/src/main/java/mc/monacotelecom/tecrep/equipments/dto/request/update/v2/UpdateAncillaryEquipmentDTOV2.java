package mc.monacotelecom.tecrep.equipments.dto.request.update.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mc.monacotelecom.tecrep.equipments.dto.OnPutAncillary;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateAncillaryEquipmentDTOV2 implements UpdateAncillaryEquipment {

    @Schema(description = "MAC Address")
    private String macAddress;

    @Schema(description = "Internal ID of the paired equipment")
    private Long pairedEquipmentId;

    @Schema(description = "Whether the ancillary equipment is independent, or has a paired equipment")
    @NotNull(groups = OnPutAncillary.class)
    private Boolean independent;

    @Schema(description = "SFP Version")
    private String sfpVersion;

    @Schema(description = "Warehouse name")
    private String warehouseName;

    @Schema(description = "Order ID")
    private String orderId;

    @Schema(description = "Service ID")
    private Long serviceId;

    @Schema(description = "Batch number")
    private String batchNumber;

    @Schema(description = "External Number")
    @Length(max = 40)
    private String externalNumber;
}

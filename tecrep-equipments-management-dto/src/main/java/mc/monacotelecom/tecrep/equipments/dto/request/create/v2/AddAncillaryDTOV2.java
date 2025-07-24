package mc.monacotelecom.tecrep.equipments.dto.request.create.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mc.monacotelecom.tecrep.equipments.enums.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddAncillaryDTOV2 {

    @Schema(description = "Serial Number")
    @Length(max = 20)
    @NotEmpty
    private String serialNumber;

    @NotBlank
    private String macAddress;

    @Schema(description = "Equipment model name")
    private String modelName;

    @Schema(description = "Whether the ancillary equipment is independent, or has a paired equipment")
    private Boolean independent;

    @Schema(description = "Internal ID of the paired equipment")
    private Long pairedEquipmentId;

    @Schema(description = "Category of the paired equipment")
    private EquipmentCategory pairedEquipmentCategory;

    @Schema(description = "External Number")
    @Length(max = 40)
    private String externalNumber;

    @Schema(description = "Access Type")
    private AccessType accessType;

    @Schema(description = "Activity")
    private Activity activity;

    @Schema(description = "Order ID")
    private String orderId;

    @Schema(description = "Warehouse storing the CPE")
    private String warehouseName;

    @Schema(description = "Date when the equipment status went from ASSIGNED to ACTIVATED")
    private LocalDateTime activationDate;

    @Schema(description = "Date when the equipment status went from BOOKED to ASSIGNED")
    private LocalDateTime assignmentDate;

    @Schema(description = "Nature")
    private EquipmentNature nature = EquipmentNature.MAIN;

    private Boolean recyclable = false;

    private Boolean preactivated;

    @Schema(description = "Batch Number")
    @Length(max = 20)
    private String batchNumber;

    @Schema(description = "Equipment name")
    private EquipmentName equipmentName;

    @Schema(description = "SFP version")
    private String sfpVersion;

    @Schema(description = "number of time equipment ancillary has been recycled")
    private Long numberRecycles = 0L;
}

package mc.monacotelecom.tecrep.equipments.dto.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentCategory;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentName;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AncillaryEquipmentDTOV2 extends EquipmentDTOV2 {

    @Schema(description = "MAC Address")
    @NotBlank
    private String macAddress;

    @Schema(description = "Whether the ancillary equipment is independent, or has a paired equipment")
    private Boolean independent;

    @Schema(description = "Internal ID of the paired equipment")
    private Long pairedEquipmentId;

    @Schema(description = "Category of the paired equipment")
    private EquipmentCategory pairedEquipmentCategory;

    @Schema(description = "Equipment name")
    private EquipmentName equipmentName;

    @Schema(description = "SFP Version")
    private String sfpVersion;

    @Schema(description = "Number of times the ancillary has been recycled")
    private Long numberRecycles = 0L;

    @Schema(description = "Related Equipment model")
    private EquipmentModelDTOV2 model;
}

package mc.monacotelecom.tecrep.equipments.dto.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentModelCategory;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EquipmentModelDTOV2 {

    @Schema(description = "Internal ID", example = "1")
    private long id;

    @NotBlank
    @Schema(description = "Name", example = "ONT")
    private String name;

    @Schema(description = "Current firmware")
    private String currentFirmware;

    @NotNull
    @Schema(description = "Related provider")
    private ProviderDTOV2 provider;

    @NotNull
    @Schema(description = "Access type", example = "DOCSIS")
    private AccessType accessType;

    @NotNull
    @Schema(description = "category", example = "CPE")
    private EquipmentModelCategory category;
    // Equipment_name is a unique identifier for the equipment model
    @Schema(description = "Equipment name", example = "BRDBOX")
    private String equipmentName;
    


}

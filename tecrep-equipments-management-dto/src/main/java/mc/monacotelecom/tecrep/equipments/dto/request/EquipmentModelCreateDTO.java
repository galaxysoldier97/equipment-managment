package mc.monacotelecom.tecrep.equipments.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentModelCategory;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentModelCreateDTO {

    @NotBlank
    @Schema(description = "name of the equipment model", example = "ONT")
    private String name;

    @Schema(description = "currentFirmware of the equipment model")
    private String currentFirmware;

    @NotNull
    @Schema(description = "providerId of the equipment model")
    private Long providerId;

    @Schema(description = "accessType of the equipment model", example = "DOCSIS")
    private AccessType accessType;

    @NotNull
    @Schema(description = "category of the equipment model ENUM('CPE', 'ANCILLARY')")
    private EquipmentModelCategory category;

    //Equipment_name is a unique identifier for the equipment model
    @Schema(description = "equipmentName of the equipment ENUM('BRDBOX','HDD','ONT','STB')")
    private String equipmentName;

 
}

package mc.monacotelecom.tecrep.equipments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated(since = "2.21.0", forRemoval = true)
public class EquipmentModelDTO {

    @Schema(description = "Internal identifier")
    private long id;

    @NotBlank
    @Schema(description = "Name", example = "ONT")
    private String name;

    @Schema(description = "Current firmware")
    private String currentFirmware;

    @NotNull
    @Schema(description = "Related provider")
    private ProviderDTO provider;

    @NotNull
    @Schema(description = "Access type", example = "DOCSIS")
    private AccessType accessType;

    @NotNull
    @Schema(description = "category", example = "CPE")
    private EquipmentModelCategory category;
}

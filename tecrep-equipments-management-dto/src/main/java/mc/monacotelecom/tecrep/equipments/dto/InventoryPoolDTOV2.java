package mc.monacotelecom.tecrep.equipments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mc.monacotelecom.tecrep.equipments.enums.SimCardSimProfile;

import javax.validation.constraints.NotBlank;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryPoolDTOV2 {

    @Schema(description = "Internal ID", example = "1")
    private Long id;

    @Schema(description = "Code", example = "METEOR_PAIRED")
    @NotBlank
    private String code;

    @Schema(description = "Description")
    private String description;

    @Schema(description = "MVNO", example = "9")
    private Integer mvno;

    @Schema(description = "Profile")
    private SimCardSimProfile simProfile;
}

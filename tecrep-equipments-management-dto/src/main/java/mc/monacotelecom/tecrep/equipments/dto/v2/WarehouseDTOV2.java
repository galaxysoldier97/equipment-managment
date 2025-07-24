package mc.monacotelecom.tecrep.equipments.dto.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WarehouseDTOV2 {

    @Schema(description = "Internal ID", example = "1")
    Long id;

    @Schema(description = "Warehouse code", example = "Monaco Telecom Entreprise")
    @NotBlank
    private String name;

    @Schema(description = "Warehouse reseller code", example = "MTENT")
    @NotBlank
    private String resellerCode;

}

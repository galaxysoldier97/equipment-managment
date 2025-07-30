package mc.monacotelecom.tecrep.equipments.dto.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HomologacionMaterialSapDTOV2 {

    @Schema(description = "Internal ID", example = "1")
    private Long id;

    @Schema(description = "Material SAP identifier")
    private String idMaterialSap;

    @Schema(description = "Material SAP name")
    private String nameSap;

    @Schema(description = "Related equipment model ID")
    private Long equipmentModelId;

    @Schema(description = "Status of the material")
    private String status;

    @Schema(description = "Related equipment model name")
    private String equipmentModelName;

    @Schema(description = "Access type of the equipment model")
    private AccessType accessType;
}

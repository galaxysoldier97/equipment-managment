package mc.monacotelecom.tecrep.equipments.dto.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PoAncillaryEquipmentSapDTOV2 {

    @Schema(description = "Internal ID", example = "1")
    private Long id;

    @Schema(description = "PO number")
    private String poNo;

    @Schema(description = "Node model")
    private String model;

    @Schema(description = "Status of the PO")
    private String status;

    @Schema(description = "Related equipment model ID")
    private Long modelId;
}
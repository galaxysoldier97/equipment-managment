package mc.monacotelecom.tecrep.equipments.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateBatchRequestDTO {
    @Schema(description = "Code of the inventory pool", example = "EPIC_POOL")
    @NotBlank
    private String inventoryPoolCode;

    @Schema(description = "Name of the SIM Card generation configuration", example = "EPIC_CONFIGURATION")
    @NotBlank
    private String configurationName;
}

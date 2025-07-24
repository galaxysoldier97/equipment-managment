package mc.monacotelecom.tecrep.equipments.dto.request.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SearchSimCardGenerationConfigurationDTO {
    @Schema(description = "Name", example = "METEOR_PAIRED")
    private String name;

    @Schema(description = "Transport Key", example = "5")
    private Integer transportKey;
}

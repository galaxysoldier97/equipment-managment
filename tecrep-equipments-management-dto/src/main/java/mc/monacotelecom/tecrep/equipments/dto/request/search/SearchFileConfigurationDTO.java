package mc.monacotelecom.tecrep.equipments.dto.request.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SearchFileConfigurationDTO {
    @Schema(description = "Name", example = "default_export")
    private String name;
}
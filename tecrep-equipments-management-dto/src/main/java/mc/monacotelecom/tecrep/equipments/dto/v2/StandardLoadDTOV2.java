package mc.monacotelecom.tecrep.equipments.dto.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StandardLoadDTOV2 {

    @Schema(description = "Internal ID", example = "1")
    private Long id;

    @Schema(description = "Name", example = "Carga estandar")
    private String name;

    @Schema(description = "Creation date")
    private LocalDateTime creationDate;

    @Schema(description = "Update date")
    private LocalDateTime updateDate;

    @Schema(description = "Status", example = "enable")
    private String status;
}


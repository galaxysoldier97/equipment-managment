package mc.monacotelecom.tecrep.equipments.dto.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProviderDTOV2 {

    @Schema(description = "Internal ID", example = "1")
    private Long id;

    @Schema(description = "Name", example = "Belgacom Mobile")
    private String name;

    @Schema(description = "Access type")
    private AccessType accessType;
}

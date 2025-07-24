package mc.monacotelecom.tecrep.equipments.dto.request.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;

@Data
public class SearchProviderDTO {
    @Schema(description = "Name", example = "Gemalto")
    private String name;

    @Schema(description = "Access Type", example = "FREEDHOME")
    private AccessType accessType;
}
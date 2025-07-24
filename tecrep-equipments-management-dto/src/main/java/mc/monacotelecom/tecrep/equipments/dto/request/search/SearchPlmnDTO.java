package mc.monacotelecom.tecrep.equipments.dto.request.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SearchPlmnDTO {
    @Schema(description = "Code", example = "20202")
    private String code;

    @Schema(description = "Prefix", example = "3368900890254")
    private String prefix;
}
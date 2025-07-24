package mc.monacotelecom.tecrep.equipments.dto.request.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mc.monacotelecom.tecrep.equipments.enums.SimCardSimProfile;

@Data
public class SearchInventoryPoolDTO {
    @Schema(example = "EPIC_PREPAY_POOL")
    private String code;

    @Schema(example = "1")
    private Integer mvno;

    @Schema(example = "REPLACEMENT")
    private SimCardSimProfile simProfile;
}
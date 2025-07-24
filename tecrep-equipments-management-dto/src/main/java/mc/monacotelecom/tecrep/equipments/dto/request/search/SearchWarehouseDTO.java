package mc.monacotelecom.tecrep.equipments.dto.request.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SearchWarehouseDTO {
    @Schema(description = "Name", example = "Univers Telecom")
    private String name;

    @Schema(description = "Reseller Code", example = "UTE")
    private String resellerCode;
}
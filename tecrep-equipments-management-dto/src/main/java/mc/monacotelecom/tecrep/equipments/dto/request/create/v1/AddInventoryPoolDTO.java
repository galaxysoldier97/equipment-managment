package mc.monacotelecom.tecrep.equipments.dto.request.create.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddInventoryPool;
import mc.monacotelecom.tecrep.equipments.enums.SimCardSimProfile;

import javax.validation.constraints.NotEmpty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated(forRemoval = true, since = "2.21")
public class AddInventoryPoolDTO implements AddInventoryPool {

    @Schema(description = "Internal ID", example = "1")
    private Long id;

    @Schema(description = "Code", example = "EPIC_POOL")
    @NotEmpty
    private String code;

    @Schema(description = "Description")
    private String description;

    @Schema(description = "MVNO", example = "9")
    private Integer mvno;

    @Schema(description = "Profile")
    private SimCardSimProfile simProfile;
}

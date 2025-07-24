package mc.monacotelecom.tecrep.equipments.dto.request.create.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mc.monacotelecom.tecrep.equipments.enums.SimCardSimProfile;

import javax.validation.constraints.NotEmpty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddInventoryPoolDTOV2 implements AddInventoryPool {

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

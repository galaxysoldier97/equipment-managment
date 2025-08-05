package mc.monacotelecom.tecrep.equipments.dto.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EquipmentModelNameDTOV2 {
    @Schema(description = "Internal ID", example = "1")
    private long id;

    @Schema(description = "Name", example = "FAST5670")
    private String name;
}
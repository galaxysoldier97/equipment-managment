package mc.monacotelecom.tecrep.equipments.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AncillaryChangeStateDTO extends ChangeStatusDto {
    @Schema(description = "Internal ID of the paired equipment, optional for actions assign/activate")
    private Long pairedEquipmentId;
}

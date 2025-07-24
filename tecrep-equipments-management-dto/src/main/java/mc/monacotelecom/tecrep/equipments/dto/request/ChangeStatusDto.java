package mc.monacotelecom.tecrep.equipments.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeStatusDto {

    @Schema(description = "Service ID, optional for actions assign/activate")
    private Long serviceId;

    @Schema(description = "Order ID, mandatory for action book")
    private String orderId;
}

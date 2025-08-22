package mc.monacotelecom.tecrep.equipments.dto;

import lombok.Data;
import mc.monacotelecom.tecrep.equipments.annotations.ExactlyOneType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.AssertTrue;

@Data
@ExactlyOneType
public class StandardLoadItemDTO {

    @NotNull
    private Long idStandard;

    private Long idModel;
    private Long idMaterial;
    private Long idGroup;

    @NotNull
    @Min(0)
    private Integer minQty;

    @NotNull
    @Min(0)
    private Integer maxQty;

    @AssertTrue(message = "maxQty must be greater than or equal to minQty")
    public boolean isQuantityValid() {
        if (minQty == null || maxQty == null) {
            return true;
        }
        return maxQty >= minQty;
    }
}

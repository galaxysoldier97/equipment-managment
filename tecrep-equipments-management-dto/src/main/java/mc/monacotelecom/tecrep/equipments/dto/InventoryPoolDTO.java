package mc.monacotelecom.tecrep.equipments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mc.monacotelecom.tecrep.equipments.enums.SimCardSimProfile;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotEmpty;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated(forRemoval = true, since = "2.21")
public class InventoryPoolDTO extends RepresentationModel<InventoryPoolDTO> {

    private Long inventoryPoolId;

    @NotEmpty
    private String code;

    private String description;

    private Integer mvno;

    private SimCardSimProfile simProfile;
}

package mc.monacotelecom.tecrep.equipments.dto.request.update;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import mc.monacotelecom.tecrep.equipments.enums.SimCardSimProfile;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateInventoryPoolDTO {

    private String code;
    private String description;
    private Integer mvno;
    private SimCardSimProfile simProfile;
}

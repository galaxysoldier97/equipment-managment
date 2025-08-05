package mc.monacotelecom.tecrep.equipments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EquipmentConfigDTO {
    private String name;
    private String value;
    private String status;
}
package mc.monacotelecom.tecrep.equipments.connectors.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IFSBoxResponse {
    private String serialNo;
    private String status;
    private String description;
}

package mc.monacotelecom.tecrep.equipments.dto.request.update;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateFileConfigurationDTO {

    private String name;
    private String prefix;
    private String suffix;
    private String headerFormat;
    private String recordFormat;
}

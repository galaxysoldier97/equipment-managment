package mc.monacotelecom.tecrep.equipments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileConfigurationDTO {

    @NotEmpty
    private String name;
    private String prefix;
    private String suffix;
    private String headerFormat;
    private String recordFormat;
}

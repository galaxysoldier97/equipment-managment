package mc.monacotelecom.tecrep.equipments.dto.request.update;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import mc.monacotelecom.tecrep.equipments.dto.PlmnDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateSimCardGenerationConfiguration;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated(since = "2.21.0", forRemoval = true)
public class UpdateSimCardGenerationConfigurationDTO implements UpdateSimCardGenerationConfiguration {

    private String name;
    private UpdateFileConfigurationDTO exportFileConfiguration;
    private UpdateFileConfigurationDTO importFileConfiguration;
    private String transportKey;
    private Integer algorithmVersion;
    private PlmnDTO plmn;
    private String msinSequence;
    private String iccidSequence;
    private String notify;
    private String artwork;
    private String simReference;
    private String type;
    private String fixedPrefix;
    private String sequencePrefix;
}

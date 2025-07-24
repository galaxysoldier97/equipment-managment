package mc.monacotelecom.tecrep.equipments.configuration;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Configuration
@ConfigurationProperties(prefix = "simcardgeneration")
@Profile("epic")
public class EpicSimCardGenerationProperties extends DefaultSimCardGenerationProperties {

    private String hlrId;
    private String cc;
    private String tr;
    private String mccmnc;

    private Map<String, String> providers = new HashMap<>();
    private Map<String, Object> brands = new HashMap<>();
    private Map<String, String> profiles = new HashMap<>();
}

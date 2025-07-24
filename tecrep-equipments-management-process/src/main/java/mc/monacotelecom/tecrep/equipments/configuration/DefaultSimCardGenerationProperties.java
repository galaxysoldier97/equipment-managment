package mc.monacotelecom.tecrep.equipments.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Data
@Configuration
@ConfigurationProperties(prefix = "simcardgeneration")
@Profile(value = "!epic")
public class DefaultSimCardGenerationProperties {

    private String formatICCID;
    private String formatMSIN;
}

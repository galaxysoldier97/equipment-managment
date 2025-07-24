package mc.monacotelecom.tecrep.equipments.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "allotmentfilename")
public class AllotmentFileNameProperties {

    private String formatBatchId;
    private String formatAllotmentNumber;
}

package mc.monacotelecom.tecrep.equipments.resources.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

@TestConfiguration
public class EqmTestConfiguration {
    @Bean("mvcConversionService")
    public ConversionService conversionService() {
        return new DefaultConversionService();
    }

}

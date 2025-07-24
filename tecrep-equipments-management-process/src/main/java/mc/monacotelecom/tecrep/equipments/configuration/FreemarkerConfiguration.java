package mc.monacotelecom.tecrep.equipments.configuration;

import freemarker.template.TemplateExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FreemarkerConfiguration {
    @Bean
    freemarker.template.Configuration configuration(TemplateExceptionHandler templateExceptionHandler) {
        var configuration = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_29);
        configuration.setTemplateExceptionHandler(templateExceptionHandler);
        return configuration;
    }

    @Bean
    TemplateExceptionHandler templateExceptionHandler() {
        return new FreemarkerExceptionHandler();
    }
}

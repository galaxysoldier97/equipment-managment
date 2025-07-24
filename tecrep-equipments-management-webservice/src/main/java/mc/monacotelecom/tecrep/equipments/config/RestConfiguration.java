package mc.monacotelecom.tecrep.equipments.config;

import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentNature;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class RestConfiguration implements WebMvcConfigurer {

    @Value("${TECREP_EQM_ALLOWED_ORIGIN:*}")
    String corsAllowedOrigin;

    @Bean
    public CorsConfigurationSource corsFilter() {
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Collections.singletonList(corsAllowedOrigin));
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept"));
        config.setExposedHeaders(Collections.singletonList("Content-Disposition"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public WebMvcConfigurer customerConverterConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addFormatters(FormatterRegistry registry) {
                final StringToEquipmentStatesConverter statusConverter = s -> Status.valueOf(s.toUpperCase());
                registry.addConverter(statusConverter);
                final StringToAccessTypeConverter accessTypeConverter = s -> AccessType.valueOf(s.toUpperCase());
                registry.addConverter(accessTypeConverter);
                final StringToEquipmentNatureConverter natureConverter = s -> EquipmentNature.valueOf(s.toUpperCase());
                registry.addConverter(natureConverter);
            }
        };
    }

    private interface StringToEquipmentStatesConverter extends Converter<String, Status> {
    }

    private interface StringToAccessTypeConverter extends Converter<String, AccessType> {
    }

    private interface StringToEquipmentNatureConverter extends Converter<String, EquipmentNature> {
    }
}

package mc.monacotelecom.tecrep.equipments.client.connector.config;

import mc.monacotelecom.common.restclient.AuthenticationManager;
import mc.monacotelecom.tecrep.equipments.client.connector.EquipmentsManagementConnector;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Creates:
 * - A EquipmentsManagementConnector bean, always
 * - A AuthenticationManager bean if missing
 * - A RestTemplate bean if missing, taking into account existing ClientHttpRequestInterceptor beans
 */
@Configuration
public class EquipmentsManagementClientConfiguration {

    @Value("${environment.webservice.tecrep-equipments-management.url}")
    protected String equipmentsManagementBaseUrl;

    @Bean
    EquipmentsManagementConnector equipmentsManagementConnector(RestTemplate restTemplate, AuthenticationManager authenticationManager) {
        return new EquipmentsManagementConnector(this.equipmentsManagementBaseUrl, restTemplate, authenticationManager);
    }

    @ConditionalOnMissingBean
    @Bean
    public AuthenticationManager authenticationManager(KeycloakSpringBootProperties properties) {
        return new AuthenticationManager(properties);
    }

    @ConditionalOnMissingBean
    @Bean
    RestTemplate restTemplate(List<ClientHttpRequestInterceptor> interceptors) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }
}

package mc.monacotelecom.tecrep.equipments.config;

import mc.monacotelecom.inventory.common.repository.CustomRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public CustomRepository customRepository(final EntityManager entityManager) {
        return new CustomRepository(entityManager);
    }
}

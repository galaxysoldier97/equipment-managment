package mc.monacotelecom.tecrep.equipments.repository;

import mc.monacotelecom.tecrep.equipments.entity.SimCardGenerationConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SimCardGenerationConfigurationRepository extends JpaRepository<SimCardGenerationConfiguration, Long>, JpaSpecificationExecutor<SimCardGenerationConfiguration> {

    Optional<SimCardGenerationConfiguration> findByName(String name);

    void deleteByName(String name);

    boolean existsByName(String name);
}

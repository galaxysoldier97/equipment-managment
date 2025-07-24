package mc.monacotelecom.tecrep.equipments.repository;

import mc.monacotelecom.tecrep.equipments.entity.FileConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileConfigurationRepository extends JpaRepository<FileConfiguration, Long>, JpaSpecificationExecutor<FileConfiguration> {
    Optional<FileConfiguration> findByName(String name);

    void deleteByName(String name);

    boolean existsByName(String name);

    @Query("SELECT f FROM FileConfiguration f JOIN SimCardGenerationConfiguration s ON s.importFileConfiguration = f")
    List<FileConfiguration> findImportFileConfigurations();
}

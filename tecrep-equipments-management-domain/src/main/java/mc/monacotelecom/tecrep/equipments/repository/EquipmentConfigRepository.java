package mc.monacotelecom.tecrep.equipments.repository;

import mc.monacotelecom.tecrep.equipments.entity.EquipmentConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipmentConfigRepository extends JpaRepository<EquipmentConfig, Long> {
    Optional<EquipmentConfig> findByNameAndStatus(String name, EquipmentConfig.Status status);

    boolean existsByName(String name);
}
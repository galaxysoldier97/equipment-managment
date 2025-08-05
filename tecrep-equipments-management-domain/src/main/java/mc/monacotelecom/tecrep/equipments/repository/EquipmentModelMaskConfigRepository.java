package mc.monacotelecom.tecrep.equipments.repository;

import mc.monacotelecom.tecrep.equipments.entity.EquipmentModelMaskConfig;
import mc.monacotelecom.tecrep.equipments.entity.EquipmentModelMaskConfig.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EquipmentModelMaskConfigRepository extends JpaRepository<EquipmentModelMaskConfig, Long> {
    Optional<EquipmentModelMaskConfig> findFirstByModelIdAndStatus(Long modelId, Status status);
}
package mc.monacotelecom.tecrep.equipments.repository;

import mc.monacotelecom.tecrep.equipments.entity.EquipmentModel;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentModelCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;

import java.util.Optional;

public interface EquipmentModelRepository extends JpaRepository<EquipmentModel, Long>, JpaSpecificationExecutor<EquipmentModel>, RevisionRepository<EquipmentModel, Long, Integer> {
    boolean existsByNameAndCategory(final String modelName, final EquipmentModelCategory category);
    Optional<EquipmentModel> findByNameAndCategory(final String modelName, final EquipmentModelCategory category);
     Optional<EquipmentModel> findByName(String name);
}

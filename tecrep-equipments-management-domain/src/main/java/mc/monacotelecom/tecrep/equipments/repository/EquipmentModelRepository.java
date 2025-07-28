package mc.monacotelecom.tecrep.equipments.repository;

import mc.monacotelecom.tecrep.equipments.entity.EquipmentModel;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentModelCategory;
import mc.monacotelecom.tecrep.equipments.projections.EquipmentModelNameProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.history.RevisionRepository;

import java.util.List;
import java.util.Optional;

public interface EquipmentModelRepository extends JpaRepository<EquipmentModel, Long>, JpaSpecificationExecutor<EquipmentModel>, RevisionRepository<EquipmentModel, Long, Integer> {
    boolean existsByNameAndCategory(final String modelName, final EquipmentModelCategory category);
    Optional<EquipmentModel> findByNameAndCategory(final String modelName, final EquipmentModelCategory category);
    Optional<EquipmentModel> findByName(String name);

    @Query("SELECT DISTINCT e.accessType FROM EquipmentModel e WHERE e.category = :category")
    List<AccessType> findDistinctAccessTypesByCategory(@Param("category") EquipmentModelCategory category);

    @Query("SELECT e.name FROM EquipmentModel e WHERE e.category = :category AND e.accessType = :accessType")
    List<String> findNamesByCategoryAndAccessType(@Param("category") EquipmentModelCategory category,
                                                  @Param("accessType") AccessType accessType);

    @Query("SELECT e.id AS id, e.name AS name FROM EquipmentModel e WHERE e.category = :category AND e.accessType = :accessType ORDER BY e.id")
    List<EquipmentModelNameProjection> findIdAndNameByCategoryAndAccessType(@Param("category") EquipmentModelCategory category,
                                                                            @Param("accessType") AccessType accessType);
}

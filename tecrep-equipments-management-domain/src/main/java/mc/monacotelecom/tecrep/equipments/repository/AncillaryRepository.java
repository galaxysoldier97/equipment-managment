package mc.monacotelecom.tecrep.equipments.repository;

import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.entity.EquipmentModel;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static mc.monacotelecom.tecrep.equipments.repository.queries.Queries.*;

public interface AncillaryRepository extends JpaRepository<AncillaryEquipment, Long>, JpaSpecificationExecutor<AncillaryEquipment>, RevisionRepository<AncillaryEquipment, Long, Integer>, ICleanAudit {

    Optional<AncillaryEquipment> findBySerialNumber(String serialNumber);

    Optional<AncillaryEquipment> findByMacAddress(String macAddress);

    Collection<AncillaryEquipment> findByBatchNumber(String batchNumber);

    Collection<AncillaryEquipment> findBySerialNumberLessThanEqualAndSerialNumberGreaterThanEqual(String finalRange, String startRange);

    Optional<AncillaryEquipment> findByCategoryAndSerialNumber(EquipmentCategory category, String serialNumber);

    Optional<AncillaryEquipment> findByPairedEquipmentSerialNumber(String serialNumber);

    Optional<AncillaryEquipment> findByServiceIdAndEquipmentIdNot(Long serviceId, Long id);

    boolean existsByModel(EquipmentModel model);
    boolean existsBySerialNumber(String serialNumber);
    boolean existsByMacAddress(String macAddress);

    @Modifying
    @Query(value = DELETE_AUDIT_ANCILARY_QUERY, nativeQuery = true)
    void cleanAudit(@Param("id") final long id);

    @Modifying
    @Override
    @Query(value = DELETE_ALL_ANCYLLARY_AUDIT_IN_QUERY, nativeQuery = true)
    void cleanAuditIdIn(@Param("ids") final Iterable<? extends Long> ids);

    @Modifying
    @Query(value = UPDATE_AUDIT_REMOVE_PAIRED_EQUIPMENT, nativeQuery = true)
    void updateAuditAfterClean(@Param("ids") final Iterable<? extends Long> ids);
}
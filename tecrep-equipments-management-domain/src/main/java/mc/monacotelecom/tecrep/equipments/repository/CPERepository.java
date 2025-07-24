package mc.monacotelecom.tecrep.equipments.repository;

import mc.monacotelecom.tecrep.equipments.entity.CPE;
import mc.monacotelecom.tecrep.equipments.entity.EquipmentModel;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentCategory;
import mc.monacotelecom.tecrep.equipments.projections.CpeUnmProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static mc.monacotelecom.tecrep.equipments.repository.queries.Queries.*;

public interface CPERepository extends JpaRepository<CPE, Long>, JpaSpecificationExecutor<CPE>, RevisionRepository<CPE, Long, Integer>, ICleanAudit {

    Optional<CPE> findByEquipmentId(Long equipmentId);

    Optional<CPE> findByCategoryAndSerialNumber(EquipmentCategory category, String serialNumber);

    Optional<CPE> findByServiceId(Long serviceId);

    Optional<CPE> findBySerialNumber(String sn);

    List<CPE> findAllByMacAddressCpe(String macAddressCpe);

    List<CPE> findAllByMacAddressVoip(String macAddressVoip);

    List<CPE> findAllByMacAddressRouter(String macAddressRouter);

    List<CPE> findAllByMacAddressLan(String macAddressLan);

    List<CPE> findAllByMacAddress5G(String macAddress5G);

    List<CPE> findAllByMacAddress4G(String macAddress24G);

    List<CPE> findAllBySerialNumber(String serialNumber);

    List<CpeUnmProjection> findAllByEquipmentIdIn(List<Long> equipmentId);

    Collection<CPE> findByBatchNumber(String batchNumber);

    Collection<CPE> findBySerialNumberLessThanEqualAndSerialNumberGreaterThanEqual(String finalRange, String startRange);

    boolean existsByModel(EquipmentModel model);

    @Modifying
    @Query(value = DELETE_AUDIT_CPE_QUERY, nativeQuery = true)
    void cleanAudit(@Param("id") final long id);

    @Modifying
    @Override
    @Query(value = DELETE_ALL_CPE_AUDIT_IN_QUERY, nativeQuery = true)
    void cleanAuditIdIn(@Param("ids") final Iterable<? extends Long> ids);
}
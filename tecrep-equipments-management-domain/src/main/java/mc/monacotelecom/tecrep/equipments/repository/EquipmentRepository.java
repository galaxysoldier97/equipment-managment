package mc.monacotelecom.tecrep.equipments.repository;

import mc.monacotelecom.tecrep.equipments.entity.Equipment;
import mc.monacotelecom.tecrep.equipments.projections.EquipmentProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static mc.monacotelecom.tecrep.equipments.repository.queries.Queries.DELETE_ALL_EQM_AUDIT_IN_QUERY;
import static mc.monacotelecom.tecrep.equipments.repository.queries.Queries.DELETE_AUDIT_EQUIPMENT_QUERY;

public interface EquipmentRepository<T extends Equipment> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T>, ICleanAudit {

    List<T> findByBatchNumber(String batchNumber);

    void deleteAllByEquipmentIdIn(Iterable<? extends Long> ids);

    Collection<Equipment> findBySerialNumberLessThanEqualAndSerialNumberGreaterThanEqual(String finalRange, String startRange);

    @Query(
            value = "select distinct lastStatusChange, " +
                    "                a.id            as equipmentId, " +
                    "                a.serial_number as serialNumber, " +
                    "                a.status, " +
                    "                a.category, " +
                    "                a.recyclable " +
                    "from (select eqm.id, " +
                    "             eqm.serial_number, " +
                    "             eqm.status, " +
                    "             eqm.category, " +
                    "             eqm.recyclable, " +
                    "             max(timestamp) over (partition by aud.id order by timestamp desc ) as lastStatusChange " +
                    "      from equipment as eqm " +
                    "               inner join equipment_aud aud on eqm.id = aud.id " +
                    "               inner join auditenversinfo on revision_id = auditenversinfo.id " +
                    "      where eqm.status = :status " +
                    "           and eqm.recyclable = :recyclable) a " +
                    "where (CURRENT_DATE - INTERVAL :days DAY) >= FROM_UNIXTIME(lastStatusChange / 1000);",
            nativeQuery = true
    )
    List<EquipmentProjection> getEquipmentCleanCandidates(@Param("status") String status, @Param("recyclable") Boolean recyclable, @Param("days") Long days);

    @Modifying
    @Query(value = DELETE_AUDIT_EQUIPMENT_QUERY, nativeQuery = true)
    void cleanAudit(@Param("id") final long id);

    @Modifying
    @Override
    @Query(value = DELETE_ALL_EQM_AUDIT_IN_QUERY, nativeQuery = true)
    void cleanAuditIdIn(@Param("ids") final Iterable<? extends Long> ids);
}

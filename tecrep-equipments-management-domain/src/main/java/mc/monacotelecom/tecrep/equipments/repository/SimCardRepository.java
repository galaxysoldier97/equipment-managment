package mc.monacotelecom.tecrep.equipments.repository;

import mc.monacotelecom.tecrep.equipments.entity.AllotmentSummary;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentCategory;
import mc.monacotelecom.tecrep.equipments.projections.SimCardProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static mc.monacotelecom.tecrep.equipments.repository.queries.Queries.DELETE_ALL_SIMCARD_AUDIT_IN_QUERY;
import static mc.monacotelecom.tecrep.equipments.repository.queries.Queries.DELETE_AUDIT_SIMCARD_QUERY;


public interface SimCardRepository extends JpaRepository<SimCard, Long>, JpaSpecificationExecutor<SimCard>, RevisionRepository<SimCard, Long, Integer>, ICleanAudit {

    Optional<SimCard> findByEquipmentId(Long equipmentId);

    Optional<SimCard> findByImsiNumber(String imsiNumber);

    Optional<SimCard> findBySerialNumber(String serialNumber);

    Optional<SimCardProjection> findProjectionByImsiNumber(String imsiNumber);

    Optional<SimCard> findBySerialNumberAndCategory(String sn, EquipmentCategory category);

    Optional<SimCardProjection> findProjectionBySerialNumberAndCategory(String sn, EquipmentCategory category);

    @Query(value = "SELECT DISTINCT * FROM simcard s INNER JOIN equipment on s.id = equipment.id WHERE CONVERT(s.imsi_number, UNSIGNED INT) >= :startRange AND CONVERT(s.imsi_number, UNSIGNED INT) <= :finalRange", nativeQuery = true)
    List<SimCard> findByRange(@Param("startRange") Long startRange, @Param("finalRange") Long finalRange);

    List<SimCard> findByServiceId(Long serviceId);

    Optional<SimCard> findByImsiSponsorNumber(String imsiSponsorNumber);

    Optional<SimCard> findByPackId(String packId);

    List<SimCard> findByAllotment(AllotmentSummary allotment);

    Collection<SimCard> findByBatchNumber(String batchNumber);

    void deleteByBatchNumber(String batchNumber);

    Collection<SimCard> findBySerialNumberLessThanEqualAndSerialNumberGreaterThanEqual(String finalRange, String startRange);

    Integer countSimCardBySerialNumberAndBatchNumber(String firstSerialNumber, String batchNumber);

    @Query("SELECT s FROM SimCard s where s.serialNumber >= :firstSerialNumber and s.allotment is null and s.number is not null and s.batchNumber = :batchNumber")
    List<SimCard> findAllotableSimcardWithNumber(@Param("firstSerialNumber") String firstSerialNumber, @Param("batchNumber") String batchNumber);

    @Query("SELECT s FROM SimCard s where s.serialNumber >= :firstSerialNumber and s.allotment is null  and s.batchNumber = :batchNumber")
    List<SimCard> findAllotableSimcardWithoutNumber(@Param("firstSerialNumber") String firstSerialNumber, @Param("batchNumber") String batchNumber);

    @Modifying
    @Override
    @Query(value = DELETE_AUDIT_SIMCARD_QUERY, nativeQuery = true)
    void cleanAudit(@Param("id") final long id);

    @Modifying
    @Override
    @Query(value = DELETE_ALL_SIMCARD_AUDIT_IN_QUERY, nativeQuery = true)
    void cleanAuditIdIn(@Param("ids") final Iterable<? extends Long> ids);
}
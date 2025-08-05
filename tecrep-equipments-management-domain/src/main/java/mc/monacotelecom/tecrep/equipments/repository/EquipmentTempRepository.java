package mc.monacotelecom.tecrep.equipments.repository;

import mc.monacotelecom.tecrep.equipments.entity.EquipmentTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;

public interface EquipmentTempRepository extends JpaRepository<EquipmentTemp, Long> {

    Optional<EquipmentTemp> findByPoAncillaryeqmSapId(Long poAncillaryeqmSapId);

    boolean existsByPoAncillaryeqmSapId(Long poAncillaryeqmSapId);

    Optional<EquipmentTemp> findByBoxSn(String boxSn);

    EquipmentTemp findFirstByOrderByOrderUploadIdDesc();

    List<EquipmentTemp> findAllByStatusAndOrderUploadId(String status, Long orderUploadId);

    List<EquipmentTemp> findAllByStatusAndUploadedBy(String status, String uploadedBy);

    List<EquipmentTemp> findAllByStatusInAndUploadedBy(List<String> statuses, String uploadedBy);

    @Modifying
    @Query("UPDATE EquipmentTemp e SET e.status = :status WHERE e.id IN :ids")
    void updateStatusByIds(@Param("status") String status, @Param("ids") List<Long> ids);

    @Modifying
    @Query("UPDATE EquipmentTemp e SET e.jobId = :jobId, e.processDate = :processDate WHERE e.id IN :ids")
    void updateValidEquipmentsInfo(@Param("jobId") Long jobId, @Param("processDate") LocalDateTime processDate, @Param("ids") List<Long> ids);

    boolean existsByBoxSn(String boxSn);
    
    @Modifying
    @Query("UPDATE EquipmentTemp e SET e.podSn = :podSn, e.partNo = :partNo, e.status = :status, " +
            "e.uploadedBy = :uploadedBy, e.sessionId = :sessionId, e.orderUploadId = :orderUploadId, " +
            "e.createdAt = :createdAt WHERE e.poAncillaryeqmSapId = :poId AND e.boxSn = :boxSn")
    int updateCsvValues(@Param("podSn") String podSn,
                        @Param("partNo") String partNo,
                        @Param("status") String status,
                        @Param("uploadedBy") String uploadedBy,
                        @Param("sessionId") String sessionId,
                        @Param("orderUploadId") Long orderUploadId,
                        @Param("createdAt") LocalDateTime createdAt,
                        @Param("poId") Long poId,
                        @Param("boxSn") String boxSn);
     
     List<EquipmentTemp> findAllByStatusAndJobId(String status, Long jobId);

     Optional<EquipmentTemp> findFirstByJobId(Long jobId);
     
}
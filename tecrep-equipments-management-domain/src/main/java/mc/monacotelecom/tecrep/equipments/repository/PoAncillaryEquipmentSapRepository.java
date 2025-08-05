package mc.monacotelecom.tecrep.equipments.repository;

import mc.monacotelecom.tecrep.equipments.entity.PoAncillaryEquipmentSap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PoAncillaryEquipmentSapRepository extends JpaRepository<PoAncillaryEquipmentSap, Long> {
     boolean existsByPoNo(String poNo);

     @Modifying
     @Query("UPDATE PoAncillaryEquipmentSap p SET p.status = :status WHERE p.id = :id")
     void updateStatusById(@Param("id") Long id, @Param("status") String status);
     
}
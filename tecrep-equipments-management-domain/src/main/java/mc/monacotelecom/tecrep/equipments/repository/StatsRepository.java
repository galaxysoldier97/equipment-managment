package mc.monacotelecom.tecrep.equipments.repository;

import mc.monacotelecom.tecrep.equipments.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StatsRepository extends JpaRepository<Equipment, Long> {
    @Query("SELECT status, count(*) FROM Equipment GROUP BY status")
    List<Object[]> statusOfEquipments();
}

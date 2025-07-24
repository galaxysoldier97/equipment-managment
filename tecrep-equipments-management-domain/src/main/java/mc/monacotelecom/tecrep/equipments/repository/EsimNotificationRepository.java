package mc.monacotelecom.tecrep.equipments.repository;

import mc.monacotelecom.tecrep.equipments.entity.EsimNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface EsimNotificationRepository extends JpaRepository<EsimNotification, Long>, JpaSpecificationExecutor<EsimNotification> {

    boolean existsByIccid(String iccid);
}

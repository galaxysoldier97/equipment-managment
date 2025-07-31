package mc.monacotelecom.tecrep.equipments.repository;

import mc.monacotelecom.tecrep.equipments.entity.HomologacionMaterialSap;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HomologacionMaterialSapRepository extends JpaRepository<HomologacionMaterialSap, Long> {
    Optional<HomologacionMaterialSap> findByIdMaterialSap(String idMaterialSap);
}

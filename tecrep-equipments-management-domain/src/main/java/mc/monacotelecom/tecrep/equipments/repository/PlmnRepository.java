package mc.monacotelecom.tecrep.equipments.repository;

import mc.monacotelecom.tecrep.equipments.entity.Plmn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PlmnRepository extends JpaRepository<Plmn, Long>, JpaSpecificationExecutor<Plmn> {

    Optional<Plmn> findByCode(String code);

    boolean existsByCode(String code);

}

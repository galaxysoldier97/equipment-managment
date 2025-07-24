package mc.monacotelecom.tecrep.equipments.repository;

import mc.monacotelecom.tecrep.equipments.entity.InventoryPool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface InventoryPoolRepository extends JpaRepository<InventoryPool, Long>, JpaSpecificationExecutor<InventoryPool> {

    Optional<InventoryPool> findByCode(String code);

    void deleteByCode(String code);

    boolean existsByCode(String code);
}

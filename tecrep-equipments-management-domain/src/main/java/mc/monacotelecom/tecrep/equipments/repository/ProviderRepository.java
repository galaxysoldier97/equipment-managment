package mc.monacotelecom.tecrep.equipments.repository;

import mc.monacotelecom.tecrep.equipments.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProviderRepository extends JpaRepository<Provider, Long>, JpaSpecificationExecutor<Provider> {

    Optional<Provider> findByName(String name);
}

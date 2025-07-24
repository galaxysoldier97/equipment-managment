package mc.monacotelecom.tecrep.equipments.repository;

import mc.monacotelecom.tecrep.equipments.entity.AncillaryImportError;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AncillaryImportErrorRepository extends JpaRepository<AncillaryImportError, Long> {

    /**
     * Obtiene todos los errores asociados a un job de importación dado su id.
     */
    List<AncillaryImportError> findAllByJob_Id(Long jobId);

}

package mc.monacotelecom.tecrep.equipments.repository;

import mc.monacotelecom.tecrep.equipments.entity.AncillaryImportJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AncillaryImportJobRepository extends JpaRepository<AncillaryImportJob, Long> {
    // El método findById(Long) ya está disponible desde JpaRepository y retorna Optional<AncillaryImportJob>
    List<AncillaryImportJob> findAllByStatus(AncillaryImportJob.JobStatus status);
}

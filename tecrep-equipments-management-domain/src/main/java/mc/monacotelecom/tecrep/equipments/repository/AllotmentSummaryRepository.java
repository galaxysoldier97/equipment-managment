package mc.monacotelecom.tecrep.equipments.repository;

import mc.monacotelecom.tecrep.equipments.entity.AllotmentSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AllotmentSummaryRepository extends JpaRepository<AllotmentSummary, Long>, JpaSpecificationExecutor<AllotmentSummary> {

    Page<AllotmentSummary> findAllByBatchBatchNumber(Long batchNumber, Pageable pageable);

    List<AllotmentSummary> findAllByBatchBatchNumberOrderByAllotmentNumberDesc(Long batchNumber);
}

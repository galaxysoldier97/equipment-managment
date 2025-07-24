package mc.monacotelecom.tecrep.equipments.repository;

import mc.monacotelecom.tecrep.equipments.entity.SequenceBatchNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface SequenceBatchNumberRepository extends JpaRepository<SequenceBatchNumber, Long>, JpaSpecificationExecutor<SequenceBatchNumber> {

    List<SequenceBatchNumber> findAllByOrderByValueAsc();

    List<SequenceBatchNumber> findAllByOrderByValueDesc();

    Optional<SequenceBatchNumber> findFirstByOrderByValueDesc();

    boolean existsByValue(Long value);

}

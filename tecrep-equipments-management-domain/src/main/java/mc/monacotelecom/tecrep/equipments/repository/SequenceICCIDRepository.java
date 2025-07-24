package mc.monacotelecom.tecrep.equipments.repository;

import mc.monacotelecom.tecrep.equipments.entity.SequenceICCID;
import mc.monacotelecom.tecrep.equipments.entity.SequenceMSIN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SequenceICCIDRepository extends JpaRepository<SequenceICCID, Long>, JpaSpecificationExecutor<SequenceICCID> {
    @Query(value = "SELECT DISTINCT s.name FROM SequenceICCID s")
    List<String> findDistinctSequenceICCIDNames();

    List<SequenceICCID> findByNameOrderByValueAsc(String name);

    List<SequenceICCID> findByNameOrderByValueDesc(String name);

    Optional<SequenceICCID> findFirstByNameOrderByValueDesc(String name);

    Optional<SequenceICCID> findFirstByValueBetween(Long start, Long end);

    boolean existsByValue(Long value);

    void deleteByName(String name);

    List<SequenceMSIN> findByName(String name);
}

package mc.monacotelecom.tecrep.equipments.repository;

import mc.monacotelecom.tecrep.equipments.entity.SequenceMSIN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SequenceMSINRepository extends JpaRepository<SequenceMSIN, Long>, JpaSpecificationExecutor<SequenceMSIN> {
    @Query(value = "SELECT DISTINCT s.name FROM SequenceMSIN s")
    List<String> findDistinctSequenceMSINNames();

    List<SequenceMSIN> findByNameOrderByValueAsc(String name);


    Optional<SequenceMSIN> findFirstByNameOrderByValueDesc(String name);

    Optional<SequenceMSIN> findFirstByValueBetween(Long start, Long end);

    boolean existsByValue(Long value);

    void deleteByName(String name);

    List<SequenceMSIN> findByName(String name);

}

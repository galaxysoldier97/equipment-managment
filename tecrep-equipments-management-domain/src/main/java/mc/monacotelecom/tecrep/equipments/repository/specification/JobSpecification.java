package mc.monacotelecom.tecrep.equipments.repository.specification;

import mc.monacotelecom.inventory.common.recycling.domain.BaseJobSpecification;
import mc.monacotelecom.tecrep.equipments.entity.JobConfiguration;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import org.springframework.data.jpa.domain.Specification;

public class JobSpecification extends BaseJobSpecification {

    public static Specification<JobConfiguration> isRecyclable(boolean recyclable){
        return (root, query, cb) -> cb.equal(root.get("recyclable"), recyclable);
    }

    public static Specification<JobConfiguration> hasStatus(Status status){
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

}

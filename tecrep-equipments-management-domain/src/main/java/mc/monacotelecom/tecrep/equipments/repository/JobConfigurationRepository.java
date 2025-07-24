package mc.monacotelecom.tecrep.equipments.repository;

import mc.monacotelecom.inventory.common.recycling.domain.BaseJobConfigurationRepository;
import mc.monacotelecom.tecrep.equipments.entity.JobConfiguration;
import mc.monacotelecom.tecrep.equipments.enums.JobRecyclingOperation;

import java.util.List;

public interface JobConfigurationRepository extends BaseJobConfigurationRepository<JobRecyclingOperation, JobConfiguration> {

    List<JobConfiguration> findAllByOperation(JobRecyclingOperation jobRecyclingOperation);
}

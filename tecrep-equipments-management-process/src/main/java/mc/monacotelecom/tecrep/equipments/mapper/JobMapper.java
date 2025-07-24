package mc.monacotelecom.tecrep.equipments.mapper;

import mc.monacotelecom.inventory.common.recycling.mapper.JobConfigurationMapper;
import mc.monacotelecom.tecrep.equipments.dto.jobconfiguration.AddJobConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.dto.jobconfiguration.JobConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.entity.JobConfiguration;
import mc.monacotelecom.tecrep.equipments.enums.JobRecyclingOperation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobMapper extends JobConfigurationMapper<JobRecyclingOperation, JobConfiguration, JobConfigurationDTO, AddJobConfigurationDTO> {
    JobConfiguration toEntity(final JobConfigurationDTO jobConfigurationDTO);

    JobConfigurationDTO toDto(final JobConfiguration jobConfiguration);

    JobConfiguration toEntity(final AddJobConfigurationDTO jobConfigurationDTO);
}

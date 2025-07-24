package mc.monacotelecom.tecrep.equipments.dto.jobconfiguration;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import mc.monacotelecom.inventory.common.recycling.dtos.BaseJobConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.enums.JobRecyclingOperation;
import mc.monacotelecom.tecrep.equipments.enums.Status;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class JobConfigurationDTO extends BaseJobConfigurationDTO<JobRecyclingOperation> {

    private Status status;
    private Boolean recyclable;
}

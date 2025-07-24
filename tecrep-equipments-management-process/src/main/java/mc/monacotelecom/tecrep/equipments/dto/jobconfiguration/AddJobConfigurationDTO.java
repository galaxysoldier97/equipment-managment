package mc.monacotelecom.tecrep.equipments.dto.jobconfiguration;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mc.monacotelecom.inventory.common.recycling.dtos.BaseAddJobConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.enums.JobRecyclingOperation;
import mc.monacotelecom.tecrep.equipments.enums.Status;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AddJobConfigurationDTO extends BaseAddJobConfigurationDTO<JobRecyclingOperation> {

    private Status status;
    private Boolean recyclable;
}

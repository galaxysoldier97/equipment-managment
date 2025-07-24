package mc.monacotelecom.tecrep.equipments.dto.request.update;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.OnPutAncillary;
import mc.monacotelecom.tecrep.equipments.dto.WarehouseDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateAncillaryEquipment;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated(since = "2.21.0", forRemoval = true)
public class UpdateAncillaryEquipmentDTO implements UpdateAncillaryEquipment {

    private String macAddress;

    private Long pairedEquipmentId;

    @NotNull(groups = OnPutAncillary.class)
    private Boolean independent;

    private String sfpVersion;

    private WarehouseDTO warehouse;

    private String orderId;

    private Long serviceId;

    private String batchNumber;

    private String externalNumber;
}

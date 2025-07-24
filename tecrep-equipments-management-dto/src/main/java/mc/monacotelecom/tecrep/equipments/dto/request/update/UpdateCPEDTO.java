package mc.monacotelecom.tecrep.equipments.dto.request.update;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.OnPutCpe;
import mc.monacotelecom.tecrep.equipments.dto.WarehouseDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateCPE;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated(since = "2.21.0", forRemoval = true)
public class UpdateCPEDTO implements UpdateCPE {

    @Length(max = 17)
    private String macAddressLan;

    @NotEmpty(groups = OnPutCpe.class)
    @Length(max = 17)
    private String macAddressCpe;

    @NotEmpty(groups = OnPutCpe.class)
    @Length(max = 17)
    private String macAddressRouter;

    @NotEmpty(groups = OnPutCpe.class)
    @Length(max = 17)
    private String macAddressVoip;

    @Length(max = 17)
    private String macAddress4G;

    @Length(max = 17)
    private String macAddress5G;

    @Length(max = 40)
    private String chipsetId;

    @Length(max = 25)
    private String modelName;

    @Length(max = 25)
    private String hwVersion;

    private Long serviceId;

    private String orderId;

    private String batchNumber;

    private String externalNumber;

    private WarehouseDTO warehouse;

    private String serialNumber;
}

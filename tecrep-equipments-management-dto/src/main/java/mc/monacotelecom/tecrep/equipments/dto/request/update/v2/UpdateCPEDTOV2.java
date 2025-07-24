package mc.monacotelecom.tecrep.equipments.dto.request.update.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateCPEDTOV2 implements UpdateCPE {

    @Length(max = 17)
    private String macAddressLan;

    @Length(max = 17)
    private String macAddressCpe;

    @Length(max = 17)
    private String macAddressRouter;

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

    private String warehouseName;

    private String serialNumber;
}

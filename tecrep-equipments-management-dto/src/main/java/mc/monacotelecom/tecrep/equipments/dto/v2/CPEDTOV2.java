package mc.monacotelecom.tecrep.equipments.dto.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CPEDTOV2 extends EquipmentDTOV2 {

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
    private String hwVersion;

    @Length(max = 25)
    private String wpaKey;

    @Schema(description = "Number of times the CPE has been recycled")
    private Long numberRecycles = 0L;

    @Schema(description = "Related Equipment model")
    private EquipmentModelDTOV2 model;
}

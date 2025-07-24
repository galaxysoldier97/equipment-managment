package mc.monacotelecom.tecrep.equipments.dto.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlmnDTOV2 implements java.io.Serializable {

    @Schema(description = "Internal ID", example = "1")
    private Long id;

    @Schema(description = "Code", example = "21210")
    private String code;

    @Schema(description = "Network name", example = "Monaco Telecom")
    private String networkName;

    @Schema(description = "Transferred Account Data Interchange Group Code", example = "MCOM1")
    private String tadigCode;

    @Schema(description = "Country ISO Code", example = "MCO")
    private String countryIsoCode;

    @Schema(description = "Country name", example = "Monaco")
    private String countryName;

    @Schema(description = "Ranges prefix", example = "3368900890541")
    private String rangesPrefix;
}

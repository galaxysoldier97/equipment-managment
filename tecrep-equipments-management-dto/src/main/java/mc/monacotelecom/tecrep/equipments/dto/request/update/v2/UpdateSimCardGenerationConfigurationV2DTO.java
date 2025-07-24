package mc.monacotelecom.tecrep.equipments.dto.request.update.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateSimCardGenerationConfigurationV2DTO implements UpdateSimCardGenerationConfiguration {

    @Schema(description = "Identifier of the configuration", example = "EIR_5G_MBB_MULTISIM")
    private String name;

    @Schema(description = "File configuration to be used at export time")
    private String exportFileConfigurationName;

    @Schema(description = "File configuration to be used at import time")
    private String importFileConfigurationName;

    @Schema(example = "010")
    private String transportKey;

    @Schema(description = "Algorithm version", example = "1")
    private Integer algorithmVersion;

    @Schema(description = "PLMN to use for IMSI creation")
    private String plmnCode;

    @Schema(description = "MSIN Sequence to use for IMSI creation", example = "DEFAULT")
    private String msinSequence;

    @Schema(description = "ICCID Sequence to use for ICCID creation", example = "DEFAULT")
    private String iccidSequence;

    @Schema(example = "EIR_SME_REP")
    private String artwork;

    @Schema(example = "MET_PP001_LTE")
    private String simReference;

    @Schema(example = "MULTISIM")
    private String type;

    @Schema(example = "8935303", description = "Mandatory for EIR, used in ICCID generation")
    private String fixedPrefix;

    @Schema(example = "0524", description = "Mandatory for EIR, used in ICCID generation")
    private String sequencePrefix;
}

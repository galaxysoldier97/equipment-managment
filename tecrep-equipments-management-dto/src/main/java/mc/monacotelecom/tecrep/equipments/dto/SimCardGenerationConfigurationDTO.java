package mc.monacotelecom.tecrep.equipments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated(since = "2.21.0", forRemoval = true)
public class SimCardGenerationConfigurationDTO {

    @Schema(description = "Identifier of the configuration", example = "EIR_5G_MBB_MULTISIM")
    @NotEmpty
    private String name;

    @Schema(description = "File configuration to be used at export time")
    @NotNull
    private FileConfigurationDTO exportFileConfiguration;

    @Schema(description = "File configuration to be used at import time")
    @NotNull
    private FileConfigurationDTO importFileConfiguration;

    @Schema(example = "010")
    private String transportKey;

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

    private Integer algorithmVersion;

    @Schema(description = "PLMN to use for IMSI creation")
    private PlmnDTO plmn;

    @Schema(description = "MSIN Sequence to use for IMSI creation", example = "DEFAULT")
    private String msinSequence;

    @Schema(description = "ICCID Sequence to use for ICCID creation", example = "DEFAULT")
    private String iccidSequence;

    private String notify;
}

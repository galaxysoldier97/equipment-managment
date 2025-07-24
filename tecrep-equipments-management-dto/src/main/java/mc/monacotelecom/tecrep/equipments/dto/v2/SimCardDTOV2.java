package mc.monacotelecom.tecrep.equipments.dto.v2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mc.monacotelecom.tecrep.equipments.dto.InventoryPoolDTOV2;
import mc.monacotelecom.tecrep.equipments.enums.SimCardSimProfile;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimCardDTOV2 extends EquipmentDTOV2 {

    @Schema(description = "IMSI Number", example = "1234567890123")
    @NotEmpty
    private String imsiNumber;

    @Schema(description = "IMSI Sponsor number")
    @Length(max = 15)
    private String imsiSponsorNumber;

    @Schema(description = "PUK code 1")
    @NotEmpty
    @Length(max = 8, min = 8)
    @Pattern(regexp = "^([0-9A-F]{8})$")
    private String puk1Code;

    @Schema(description = "PIN code 1")
    @NotEmpty
    @Length(min = 4, max = 4)
    @Pattern(regexp = "^([0-9A-F]{4})$")
    private String pin1Code;

    @Schema(description = "PUK code 2")
    @NotEmpty
    @Length(min = 8, max = 8)
    @Pattern(regexp = "^([0-9A-F]{8})$")
    private String puk2Code;

    @Schema(description = "PIN code 2")
    @Length(min = 4, max = 4)
    @Pattern(regexp = "^([0-9A-F]{4})$")
    private String pin2Code;

    @Schema(description = "Authorization key")
    @Length(max = 32)
    private String authKey;

    @Schema(description = "Access control class")
    @Length(max = 4)
    private String accessControlClass;

    @Schema(description = "SIM Card profile")
    private String simProfile;

    @JsonIgnore
    @Length(max = 32)
    private String otaCipheringKey;

    @JsonIgnore
    @Length(max = 32)
    private String otaSignatureKey;

    @JsonIgnore
    @Length(max = 32)
    private String putDescriptionKey;

    @JsonIgnore
    @Length(max = 32)
    private String adminCode;

    @Schema(description = "Associated number")
    @Length(max = 32)
    private String number;

    @Schema(description = "Public Land Mobile Network")
    @NotNull
    private PlmnDTOV2 plmn;

    @Schema(description = "Provider having generated the SIM Card")
    @NotNull
    private ProviderDTOV2 provider;

    @Schema
    private String packId;

    @Schema
    private String transportKey;

    @Schema
    private Integer algorithmVersion;

    @Schema
    private Integer checkDigit;

    @Schema
    private String brand;

    @Schema
    private InventoryPoolDTOV2 inventoryPool;

    @Schema(description = "Allotment ID", example = "345")
    private String allotmentId;

    @Schema(description = "If the given sim is an eSim")
    private boolean esim;

    @Schema(description = "Salt used for ota")
    private String otaSalt;

    @Schema(description = "code requesting activation of simcard")
    private String activationCode;

    @Schema(description = "code of confirmed activation of simcard")
    private Integer confirmationCode;

    @Schema(description = "Qr code linked to the simcard")
    private String qrCode;
}

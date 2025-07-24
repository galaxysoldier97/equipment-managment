package mc.monacotelecom.tecrep.equipments.dto.request.update;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateSimCard;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.Activity;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentNature;
import mc.monacotelecom.tecrep.equipments.enums.SimCardSimProfile;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Schema(description = "All details about the SimCard")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated(since = "2.21.0", forRemoval = true)
public class UpdateSimCardDTO implements UpdateSimCard {

    @Schema(description = "The IMSI Number", example = "1234567890123")
    @NotEmpty
    @Length(max = 15)
    private String imsiNumber;

    @Schema(description = "The IMSI Sponsor number")
    @Length(max = 15)
    private String imsiSponsorNumber;

    @Length(max = 20)
    private String serialNumber;

    @Schema(description = "The puk code 1")
    @NotEmpty
    @Length(max = 8, min = 8)
    @Pattern(regexp = "^([0-9A-F]{8})$")
    private String puk1Code;

    @Schema(description = "The pin code 1")
    @NotEmpty
    @Length(min = 4, max = 4)
    @Pattern(regexp = "^([0-9A-F]{4})$")
    private String pin1Code;

    @Schema(description = "The puk code 2")
    @Length(min = 8, max = 8)
    @Pattern(regexp = "^([0-9A-F]{8})$")
    private String puk2Code;

    @Schema(description = "The pin code 2")
    @NotEmpty
    @Length(min = 4, max = 4)
    @Pattern(regexp = "^([0-9A-F]{4})$")
    private String pin2Code;

    @Schema(description = "The authorization key")
    @Length(max = 32)
    private String authKey;

    @Schema(description = "The access control class")
    @Length(max = 4)
    private String accessControlClass;

    @Schema(description = "The simcard profile")
    private String simProfile;

    @Schema(description = "The service Id")
    private Long serviceId;

    @Schema(description = "The order Id")
    private String orderId;

    @Schema(description = "The warehouse Id")
    private Long warehouseId;

    @Schema
    private String packId;

    @Schema(description = "MSISDN")
    private String number;

    @Schema(description = "Batch number linked with this simCard")
    private String batchNumber;

    @Schema(description = "External number")
    private String externalNumber;

    @Schema(description = "Access Type")
    private AccessType accessType;

    private Activity activity;

    @Schema(description = "Nature of the SIM Card")
    private EquipmentNature nature;

    @Schema(description = "Indicates ff the given SIM is an eSIM")
    private Boolean esim;

    @Schema(description = "Brand of simcard")
    private String brand;
}

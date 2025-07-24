package mc.monacotelecom.tecrep.equipments.dto.request.update.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.Activity;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentNature;
import mc.monacotelecom.tecrep.equipments.enums.SimCardSimProfile;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateSimCardDTOV2 implements UpdateSimCard {

    @Schema(description = "IMSI Number", example = "1234567890123")
    @NotEmpty
    @Length(max = 15)
    private String imsiNumber;

    @Schema(description = "IMSI Sponsor number")
    @Length(max = 15)
    private String imsiSponsorNumber;

    @Length(max = 20)
    private String serialNumber;

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
    @Length(min = 8, max = 8)
    @Pattern(regexp = "^([0-9A-F]{8})$")
    private String puk2Code;

    @Schema(description = "PIN code 2")
    @NotEmpty
    @Length(min = 4, max = 4)
    @Pattern(regexp = "^([0-9A-F]{4})$")
    private String pin2Code;

    @Schema(description = "Authorization key")
    @Length(max = 32)
    private String authKey;

    @Schema(description = "Access control class")
    @Length(max = 4)
    private String accessControlClass;

    @Schema(description = "Simcard profile")
    private String simProfile;

    @Schema(description = "Service Id")
    private Long serviceId;

    @Schema(description = "Order Id")
    private String orderId;

    @Schema(description = "Warehouse name")
    private String warehouseName;

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

    @Schema(description = "code requesting activation of simcard")
    private String activationCode;

    @Schema(description = "code of confirmed activation of simcard")
    private Integer confirmationCode;

    @Schema(description = "Qr code linked to the simCar")
    private String qrCode;
}

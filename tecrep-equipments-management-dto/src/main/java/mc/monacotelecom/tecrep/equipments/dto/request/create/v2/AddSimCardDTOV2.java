package mc.monacotelecom.tecrep.equipments.dto.request.create.v2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.Activity;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentNature;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddSimCardDTOV2 {

    @Schema(description = "Serial number / ICCID")
    @Length(max = 20)
    @NotEmpty
    private String serialNumber;

    @Schema(description = "IMSI Number", example = "1234567890123")
    @NotEmpty
    private String imsiNumber;

    @Schema(description = "IMSI Sponsor number")
    @Length(max = 15)
    private String imsiSponsorNumber;

    @Schema(description = "Nature")
    private EquipmentNature nature = EquipmentNature.MAIN;

    @Schema(description = "If the given sim is an eSim")
    private boolean esim;

    @Schema(description = "Order ID")
    private String orderId;

    @Schema(description = "Service ID")
    private Long serviceId;

    @Schema(description = "Access type")
    private AccessType accessType;

    @Schema(description = "Activity")
    private Activity activity;

    @Schema(description = "Associated number")
    @Length(max = 32)
    private String number;

    @Schema(description = "Public Land Mobile Network")
    @NotNull
    private String plmnCode;

    @Schema(description = "Batch number")
    @Length(max = 20)
    private String batchNumber;

    @Schema(description = "Provider having generated the SIM Card")
    @NotBlank
    private String providerName;

    @Schema(description = "Warehouse storing the SIM")
    private String warehouseName;

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

    @Schema(description = "OTA ciphering key")
    @JsonIgnore
    @Length(max = 32)
    private String otaCipheringKey;

    @Schema(description = "OTA signature key")
    @JsonIgnore
    @Length(max = 32)
    private String otaSignatureKey;

    @Schema(description = "PUT description key")
    @JsonIgnore
    @Length(max = 32)
    private String putDescriptionKey;

    @Schema(description = "Admin code")
    @JsonIgnore
    @Length(max = 32)
    private String adminCode;

    private Boolean preactivated;

    @Schema(description = "Package ID")
    private String packId;

    @Schema(description = "Transport Key")
    private String transportKey;

    @Schema(description = "Algorithm Version")
    private Integer algorithmVersion;

    @Schema(description = "Check Digit for ICCID validation")
    private Integer checkDigit;

    @Schema(description = "Brand of simcard")
    private String brand;

    @Schema(description = "Inventory Pool code")
    private String inventoryPoolCode;

    @Schema(description = "Allotment ID", example = "345")
    private String allotmentId;

    @Schema(description = "Salt used for ota")
    private String otaSalt;

    @Schema(description = "External number")
    @Length(max = 40)
    private String externalNumber;

    @Schema(description = "Date when the equipment status passed from ASSIGNED to ACTIVATED")
    private LocalDateTime activationDate;

    @Schema(description = "Date when the equipment status passed from BOOKED to ASSIGNED")
    private LocalDateTime assignmentDate;

    @Schema(description = "code requesting activation of simcard")
    private String activationCode;

    @Schema(description = "code of confirmed activation of simcard")
    private Integer confirmationCode;
}

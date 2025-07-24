package mc.monacotelecom.tecrep.equipments.dto.request.create.v1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mc.monacotelecom.tecrep.equipments.dto.InventoryPoolDTO;
import mc.monacotelecom.tecrep.equipments.dto.PlmnDTO;
import mc.monacotelecom.tecrep.equipments.dto.ProviderDTO;
import mc.monacotelecom.tecrep.equipments.dto.WarehouseDTO;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.Activity;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentNature;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated(since = "2.21.0", forRemoval = true)
public class AddSimCardDTO {
    private String orderId;

    @Length(max = 20)
    @NotEmpty
    private String serialNumber;

    @Length(max = 40)
    private String externalNumber;

    private AccessType accessType;

    private Activity activity;

    @Schema(description = "date when the equipment status passed from ASSIGNED to ACTIVATED")
    private LocalDateTime activationDate;

    @Schema(description = "date when the equipment status passed from BOOKED to ASSIGNED")
    private LocalDateTime assignmentDate;

    private EquipmentNature nature = EquipmentNature.MAIN;

    private Boolean preactivated;

    @Length(max = 20)
    private String batchNumber;

    @Schema(description = "Provider having generated the SIM Card")
    @NotNull
    private ProviderDTO provider;

    private WarehouseDTO warehouse;

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
    private PlmnDTO plmn;

    @Schema(description = "Service ID")
    private Long serviceId;

    @Schema
    private String packId;

    @Schema
    private String transportKey;

    @Schema
    private Integer algorithmVersion;

    @Schema
    private Integer checkDigit;

    @Schema
    private InventoryPoolDTO inventoryPool;

    @Schema(description = "Allotment ID", example = "345")
    private String allotmentId;

    @Schema(description = "If the given sim is an eSim")
    private boolean esim;

    @Schema(description = "Salt used for ota")
    private String otaSalt;
}

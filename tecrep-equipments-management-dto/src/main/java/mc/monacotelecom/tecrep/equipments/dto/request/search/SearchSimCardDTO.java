package mc.monacotelecom.tecrep.equipments.dto.request.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentNature;
import mc.monacotelecom.tecrep.equipments.enums.SimCardSimProfile;
import mc.monacotelecom.tecrep.equipments.enums.Status;

import javax.validation.constraints.Pattern;

@Data
public class SearchSimCardDTO {
    @Schema(description = "International Mobile Subscriber Identity", example = "280101434046576")
    private String imsi;

    @Schema(description = "Serial number (ICCID)", example = "8935743411196513071")
    private String sn;

    @Schema(description = "ICCID of the related equipment", example = "8935743411209230267")
    private String psn;

    @Schema(example = "DEACTIVATED")
    private Status status;

    @Schema(description = "IMSI Sponsor Number", example = "280101434046576")
    private String imsisn;

    @Schema(example = "MAIN")
    private EquipmentNature nature;

    @Schema(description = "Related Provider name", example = "Gemalto")
    private String provider;

    @Schema(description = "Related Warehouse name", example = "Monaco Telecom Entreprise")
    private String warehouse;

    @Schema(description = "If the SIM Card is associated to a service")
    private Boolean service;

    @Schema(description = "Related Service Id", example = "789")
    private String serviceId;

    @Schema(description = "If the SIM Card is associated to an Order")
    private Boolean order;

    @Schema(description = "Related Order Id", example = "1NEKD9NJ")
    private String orderId;

    private String plmnCode;

    @Pattern(regexp = "^([0-9A-F]{8})$")
    @Schema(description = "puk1 code related to simcard", example = "58965684")
    private String puk1Code;

    @Pattern(regexp = "^([0-9A-F]{8})$")
    @Schema(description = "puk2 code related to simcard", example = "29792397")
    private String puk2Code;

    @Pattern(regexp = "^([0-9A-F]{4})$")
    @Schema(description = "pin1 code related to simcard", example = "2989")
    private String pin1Code;

    @Schema(description = "pin2 code related to simcard", example = "4365")
    private String pin2Code;

    @Schema(description = "sim_profile")
    private String simProfile;

    @Schema(example = "DOCSIS")
    private AccessType accessType;

    @Schema(example = "37799900001")
    private String externalNumber;

    @Schema(description = "If the SIM Card is preactivated")
    private Boolean preactivated;

    @Schema(description = "Batch Number", example = "15")
    private String batchNumber;

    @Schema(description = "Pack ID", example = "789541")
    private String packId;

    @Schema(description = "Inventory Pool Code", example = "EPIC_PREPAY_POOL")
    private String inventoryPoolCode;

    @Schema(description = "Inventory Pool ID", example = "478")
    private String inventoryPoolId;

    @Schema(description = "Allotment ID", example = "345")
    private String allotmentId;

    @Schema(description = "Phone number", example = "33678630000")
    private String number;

    @Schema(description = "If the given sim is an eSim")
    private Boolean esim;

    @Schema(description = "Brand of simcard")
    private String brand;
}
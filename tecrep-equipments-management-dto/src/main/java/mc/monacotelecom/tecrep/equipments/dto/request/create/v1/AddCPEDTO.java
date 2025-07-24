package mc.monacotelecom.tecrep.equipments.dto.request.create.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mc.monacotelecom.tecrep.equipments.dto.EquipmentModelDTO;
import mc.monacotelecom.tecrep.equipments.dto.ProviderDTO;
import mc.monacotelecom.tecrep.equipments.dto.WarehouseDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddCPE;
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
public class AddCPEDTO implements AddCPE {

    @Schema(description = "Order ID")
    private String orderId;

    @Schema(description = "Serial Number")
    @Length(max = 20)
    @NotEmpty
    private String serialNumber;

    @Schema(description = "External Number")
    @Length(max = 40)
    private String externalNumber;

    @Schema(description = "Access Type")
    private AccessType accessType;

    @Schema(description = "Activity")
    private Activity activity;

    @Schema(description = "Date when the equipment status went from ASSIGNED to ACTIVATED")
    private LocalDateTime activationDate;

    @Schema(description = "Date when the equipment status went from BOOKED to ASSIGNED")
    private LocalDateTime assignmentDate;

    @Schema(description = "Nature")
    private EquipmentNature nature = EquipmentNature.MAIN;

    private Boolean preactivated;

    @Schema(description = "Batch Number")
    @Length(max = 20)
    private String batchNumber;

    @Schema(description = "Provider having generated the CPE")
    @NotNull
    private ProviderDTO provider;

    @Schema(description = "Warehouse storing the CPE")
    private WarehouseDTO warehouse;

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

    @Schema(description = "Chipset ID")
    @Length(max = 40)
    private String chipsetId;

    @Length(max = 25)
    private String modelName;

    @Schema(description = "HW Version")
    @Length(max = 25)
    private String hwVersion;

    @Schema(description = "WPA Key")
    @Length(max = 25)
    private String wpaKey;

    @Schema(description = "Service ID")
    private Long serviceId;

    @Schema(description = "Number of times the CPE has been recycled")
    private Long numberRecycles = 0L;

    @Schema(description = "Related Equipment model")
    private EquipmentModelDTO model;
}

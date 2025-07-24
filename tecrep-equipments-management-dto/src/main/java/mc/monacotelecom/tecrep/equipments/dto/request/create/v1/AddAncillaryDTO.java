package mc.monacotelecom.tecrep.equipments.dto.request.create.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mc.monacotelecom.tecrep.equipments.dto.EquipmentModelDTO;
import mc.monacotelecom.tecrep.equipments.dto.ProviderDTO;
import mc.monacotelecom.tecrep.equipments.dto.WarehouseDTO;
import mc.monacotelecom.tecrep.equipments.enums.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated(since = "2.21.0", forRemoval = true)
public class AddAncillaryDTO {
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

    private Boolean recyclable = false;

    private Boolean preactivated;

    @Length(max = 20)
    private String batchNumber;

    @Schema(description = "Provider having generated the SIM Card")
    @NotNull
    private ProviderDTO provider;

    private WarehouseDTO warehouse;

    @NotBlank
    private String macAddress;

    private String modelName;

    private Long pairedEquipmentId;

    private Boolean independent;

    private EquipmentCategory pairedEquipmentCategory;

    private EquipmentName equipmentName;

    private String sfpVersion;

    @Schema(description = "number of time equipment ancillary has been recycled")
    private Long numberRecycles = 0L;

    @Schema(description = "Related Equipment model")
    private EquipmentModelDTO model;
}

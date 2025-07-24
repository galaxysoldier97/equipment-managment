package mc.monacotelecom.tecrep.equipments.dto.v2;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mc.monacotelecom.tecrep.equipments.enums.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EquipmentDTOV2 {

    @Schema(description = "Internal ID")
    private Long id;

    @Schema(description = "Order ID")
    private String orderId;

    @Schema(description = "Service ID")
    private Long serviceId;

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

    @Schema(description = "Date when the equipment status passed from ASSIGNED to ACTIVATED")
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss"
    )
    private LocalDateTime activationDate;

    @Schema(description = "Date when the equipment status passed from BOOKED to ASSIGNED")
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss"
    )
    private LocalDateTime assignmentDate;

    @Schema(description = "Status")
    private Status status;

    @Schema(description = "Nature")
    private EquipmentNature nature = EquipmentNature.MAIN;

    @Schema(description = "Whether the equipment is recyclable, meaning can be made available again after being deactivated")
    private Boolean recyclable = false;

    private Boolean preactivated;

    @Schema(description = "Batch number")
    @Length(max = 20)
    private String batchNumber;

    @Schema(description = "Warehouse")
    private WarehouseDTOV2 warehouse;

    @Schema(description = "Category")
    private EquipmentCategory category;

    @Schema(description = "Available transitions")
    private List<Event> events;
}

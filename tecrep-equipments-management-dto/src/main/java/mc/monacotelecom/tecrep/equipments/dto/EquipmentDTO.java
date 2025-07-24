package mc.monacotelecom.tecrep.equipments.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mc.monacotelecom.tecrep.equipments.enums.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Relation(collectionRelation = "equipments")
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated(since = "2.21.0", forRemoval = true)
public class EquipmentDTO extends RepresentationModel<EquipmentDTO> {

    private Long equipmentId;
    private String orderId;

    @Length(max = 20)
    @NotEmpty
    private String serialNumber;

    @Length(max = 40)
    private String externalNumber;

    private AccessType accessType;

    private Activity activity;

    @Schema(description = "date when the equipment status passed from ASSIGNED to ACTIVATED")
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss"
    )
    private LocalDateTime activationDate;

    @Schema(description = "date when the equipment status passed from BOOKED to ASSIGNED")
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss"
    )
    private LocalDateTime assignmentDate;

    private Status status;

    private EquipmentNature nature = EquipmentNature.MAIN;

    private Boolean recyclable = false;

    private Boolean preactivated;

    @Length(max = 20)
    private String batchNumber;

    @Schema(description = "Provider having generated the SIM Card")
    @NotNull
    private ProviderDTO provider;

    private WarehouseDTO warehouse;

    private EquipmentCategory category;

    @Schema(description = "Available transitions")
    private List<Event> events;
}

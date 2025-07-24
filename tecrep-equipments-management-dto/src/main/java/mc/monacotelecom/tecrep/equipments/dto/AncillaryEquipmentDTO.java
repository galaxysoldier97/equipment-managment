package mc.monacotelecom.tecrep.equipments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentCategory;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentName;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Relation(collectionRelation = "ancillaryequipments")
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated(since = "2.21.0", forRemoval = true)
public class AncillaryEquipmentDTO extends EquipmentDTO {

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

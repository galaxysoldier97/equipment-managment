package mc.monacotelecom.tecrep.equipments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Map;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Relation(collectionRelation = "equipmentsstats")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EquipmentsStatsDto extends RepresentationModel<EquipmentsStatsDto> {

    Map<Status, Long> statusOfEquipments;

}

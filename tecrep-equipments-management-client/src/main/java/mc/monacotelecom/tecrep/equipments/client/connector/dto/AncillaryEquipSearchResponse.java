package mc.monacotelecom.tecrep.equipments.client.connector.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.AncillaryEquipmentDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Deprecated(since = "2.21.0", forRemoval = true)
public class AncillaryEquipSearchResponse {
    @JsonProperty("ancillaryequipments")
    List<AncillaryEquipmentDTO> ancillaryEquipments;
}

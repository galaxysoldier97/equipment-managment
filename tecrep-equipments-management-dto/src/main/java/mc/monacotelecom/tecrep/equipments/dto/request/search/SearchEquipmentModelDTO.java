package mc.monacotelecom.tecrep.equipments.dto.request.search;

import lombok.Data;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentModelCategory;

@Data
public class SearchEquipmentModelDTO {
    private EquipmentModelCategory category;
}

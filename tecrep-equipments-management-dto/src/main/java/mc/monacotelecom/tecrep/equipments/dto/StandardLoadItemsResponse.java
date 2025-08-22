package mc.monacotelecom.tecrep.equipments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandardLoadItemsResponse {
    private Long standardId;
    private List<ModelItem> model;
    private List<MaterialItem> material;
    private List<GroupItem> group;
    private Counts counts;
}

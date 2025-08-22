package mc.monacotelecom.tecrep.equipments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelItem {
    private Long id;
    private Long idModel;
    private Integer minQty;
    private Integer maxQty;
    private Instant createdAt;
    private String modelName;
}

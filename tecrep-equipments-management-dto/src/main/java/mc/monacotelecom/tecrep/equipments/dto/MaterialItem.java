package mc.monacotelecom.tecrep.equipments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialItem {
    private Long id;
    private Long idMaterial;
    private Integer minQty;
    private Integer maxQty;
    private Instant createdAt;
    private String materialName;
}

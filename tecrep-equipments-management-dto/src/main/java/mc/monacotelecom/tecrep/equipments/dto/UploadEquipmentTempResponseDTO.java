package mc.monacotelecom.tecrep.equipments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadEquipmentTempResponseDTO {
    private Long orderUploadId;

    private List<String> notFoundBoxSns;

    public UploadEquipmentTempResponseDTO(Long orderUploadId) {
        this.orderUploadId = orderUploadId;
    }

    public UploadEquipmentTempResponseDTO(Long orderUploadId, List<String> notFoundBoxSns) {
        this.orderUploadId = orderUploadId;
        this.notFoundBoxSns = notFoundBoxSns;
    }
    
}
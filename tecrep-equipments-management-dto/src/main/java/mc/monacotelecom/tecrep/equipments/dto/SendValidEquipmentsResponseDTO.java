package mc.monacotelecom.tecrep.equipments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SendValidEquipmentsResponseDTO {
    private Long jobId;
    private String status;
    private String message;
}
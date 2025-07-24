package mc.monacotelecom.tecrep.equipments.dto.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.SimCardPartialDTO;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "Output parameters from export for generation of SIM cards")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenerateSimCardsResponseDTOV2 {
    private String filename;
    private SimCardGenerationConfigurationDTOV2 configuration;
    private List<SimCardPartialDTO> simCards = new ArrayList<>();
}

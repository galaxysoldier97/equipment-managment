package mc.monacotelecom.tecrep.equipments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "Output parameters from export for generation of SIM cards")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenerateSimCardsResponseDTO {
    private String filename;
    private SimCardGenerationConfigurationDTO configuration;
    private List<SimCardPartialDTO> simCards = new ArrayList<>();
}

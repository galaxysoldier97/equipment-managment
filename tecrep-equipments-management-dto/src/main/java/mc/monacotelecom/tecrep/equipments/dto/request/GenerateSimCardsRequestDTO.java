package mc.monacotelecom.tecrep.equipments.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mc.monacotelecom.tecrep.equipments.annotations.IsEpicProfile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Data
public class GenerateSimCardsRequestDTO {
    @Schema(description = "Quantity of SIM Cards to generate", example = "100")
    @Min(1)
    @NotNull
    private Integer quantity;

    @Schema(description = "Optional list of numbers to associate with the freshly-created SIM Cards")
    private List<String> numbers = new ArrayList<>();

    @IsEpicProfile
    @Schema(description = "brand of the simCard", example = "PREPAID")
    private String brand;

    @IsEpicProfile
    @Schema(description = "Type of the simCard", example = "NANO")
    private String simCardType;

    @IsEpicProfile
    @Schema(description = "Provider of the simCard", example = "GEMPLUS")
    private String provider;

    @IsEpicProfile
    @Schema(description = "Capacity of the simCard", example = "32K")
    private String profile;

    @Schema(description = "Set the Esim value of the generated Sim cards", example = "false")
    private boolean esim = false;

}

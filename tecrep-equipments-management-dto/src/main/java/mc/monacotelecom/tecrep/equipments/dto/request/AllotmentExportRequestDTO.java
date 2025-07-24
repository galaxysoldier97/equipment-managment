package mc.monacotelecom.tecrep.equipments.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AllotmentExportRequestDTO {
    @NotNull
    @Schema(description = "ID of the allotment to be exported", example = "11", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long allotmentId;

    @NotBlank
    @Schema(description = "Name of the file configuration to be used, containing the templates", example = "ExportAllotment", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fileConfigurationName;
}

package mc.monacotelecom.tecrep.equipments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsimNotificationIngress {

    @NotNull
    @Size(min = 19, max = 20)
    @Schema(description = "identities the Profile to download and install")
    private String iccid;
    @Schema(description = "Identifies the Profile Type to download and install")
    private String profileType;
    @Schema(description = "Indicates the date/time when the operation has been performed")
    private String timestamp;
    @Schema(description = "Indicates the step reached within the Profile Download and Installation procedure")
    private Integer notificationPointId;
    private String checkPoint;
    @Schema(description = "Indicates the status after the execution of the notification point")
    private ExecutionStatus notificationPointStatus;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExecutionStatus {
        private String status;
        private StatusCodeData statusCodeData;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StatusCodeData {
        private String message;
        private String reasonCode;
    }
}

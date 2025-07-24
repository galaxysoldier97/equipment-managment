package mc.monacotelecom.tecrep.equipments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Max;
import java.time.LocalDateTime;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ESimNotificationDTO {

    @Schema(description = "Internal ID")
    private Long id;

    @Schema(description = "Id of the equipment linked with this notification")
    private Long equipmentId;

    @Max(20)
    @Schema(description = "Serial number")
    private String iccid;

    @Schema(description = "Profile type to downloand and isntall")
    private String profileType;

    @Schema(description = "Date when the operation has been performed")
    private LocalDateTime timeStamp;

    @Schema(description = "Step reached within the profile downloand and installation procedure")
    private Long notificationPointId;

    @Schema(description = "Setting notification point id")
    private String checkPoint;

    @Schema(description = "Status after the execution of the notification point")
    private String status;

    @Schema(description = "Message error")
    private String message;

    @Schema(description = "Reason code error")
    private String reasonCode;

    @Schema(description = "Date Notification")
    private LocalDateTime date;
}

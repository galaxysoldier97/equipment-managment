package mc.monacotelecom.tecrep.equipments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO to request a notification.")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationRequestDTO {

    @NotBlank
    @Schema(description = "String reference for notification", example = "WELCOME_MESSAGE", requiredMode = Schema.RequiredMode.REQUIRED)
    private String reference;

    @NotNull
    @Schema(description = "A sms should be sent", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean smsNotification;

    @NotNull
    @Schema(description = "An email should be sent)", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean emailNotification;

    @NotNull
    @Schema(description = "A web notification should be sent", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean webNotification;

    @Schema(description = "Msisdn (mandatory for SMS templates)", example = "0804050405")
    private String msisdn;

    @Email
    @Schema(description = "Email address (mandatory for email templates)", example = "arthur.fleck@gmail.com")
    private String email;

    @NotBlank
    @Schema(description = "Contact unique id(mandatory for web-notifications)", example = "fff423-dv344-fdv4-vdfv3-rvf43", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contactUuid;

    @Schema(description = "Content of the message")
    private Map<String, Object> message;

    @Schema(description = "Date when message should be sent - if null (or in the past) then current date will be used.", example = "2019-11-21T12:53:11.465Z")
    private ZonedDateTime scheduledAt;

    @NotNull
    @Schema(description = "Language", example = "en_IE", requiredMode = Schema.RequiredMode.REQUIRED)
    private Locale locale;


    @Override
    public String toString() {
        return "NotificationRequest{" +
                "reference='" + this.reference + '\'' +
                ", smsNotification=" + this.smsNotification +
                ", emailNotification=" + this.emailNotification +
                ", webNotification=" + this.webNotification +
                ", msisdn='" + this.msisdn + '\'' +
                ", email='" + this.email + '\'' +
                ", contactUuid='" + this.contactUuid + '\'' +
                ", message=" + this.message +
                ", scheduledAt=" + this.scheduledAt +
                ", locale=" + this.locale +
                '}';
    }

    public static class Builder {
        private Map<String, Object> message;
        private boolean emailNotification;
        private String reference;
        private String email;
        private ZonedDateTime scheduledAt;
        private Locale locale;

        public static Builder aNotificationRequest() {
            return new Builder();
        }

        public Builder withMessage(final Map<String, Object> message) {
            this.message = message;
            return this;
        }

        public Builder forReference(final String reference) {
            this.reference = reference;
            return this;
        }

        public Builder scheduledAt(final ZonedDateTime scheduledAt) {
            this.scheduledAt = scheduledAt;
            return this;
        }

        public Builder withLocale(final Locale locale) {
            this.locale = locale;
            return this;
        }

        public Builder isEmail(final String recipientEmailAddress) {
            this.email = recipientEmailAddress;
            this.emailNotification = true;
            return this;
        }

        public NotificationRequestDTO build() {
            return new NotificationRequestDTO(this.reference,
                    false,
                    this.emailNotification,
                    false,
                    null,
                    this.email,
                    null,
                    this.message,
                    this.scheduledAt,
                    this.locale);
        }
    }
}

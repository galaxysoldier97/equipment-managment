package mc.monacotelecom.tecrep.equipments.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import mc.monacotelecom.tecrep.equipments.dto.EsimNotificationIngress;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import javax.validation.ValidationException;

public class HelperTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private HelperTest() {
    }

    public static Message sendMessage(Object object) {
        try {
            byte[] payload = objectMapper.writeValueAsBytes(object);
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            return new Message(payload, messageProperties);
        } catch (Exception e) {
            throw new ValidationException("Error while sending message" + e.getMessage());
        }
    }

    public static EsimNotificationIngress eSimNotificationBuild() {
        var ingress = new EsimNotificationIngress();
        var statusCodeData = new EsimNotificationIngress.StatusCodeData("Execution status message", "102");
        var notificationPointStatus = new EsimNotificationIngress.ExecutionStatus("STATUS_CODE", statusCodeData);
        ingress.setIccid("893771033000000007");
        ingress.setProfileType("TypeA");
        ingress.setNotificationPointId(1);
        ingress.setCheckPoint("installation");
        ingress.setTimestamp("2024-04-04T11:01:58.460");
        ingress.setNotificationPointStatus(notificationPointStatus);

        return ingress;
    }
}

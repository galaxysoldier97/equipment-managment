package mc.monacotelecom.tecrep.equipments.messagging.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.tecrep.equipments.dto.EsimNotificationIngress;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmMessagingException;
import mc.monacotelecom.tecrep.equipments.messagging.handler.EsimNotificationHandler;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class EsimNotificationListener {

    private final EsimNotificationHandler esimNotificationHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String queueName = "${esim.notification.queue:tecrep_eqm_notification_smdp_esim-request}";


    @RabbitListener(queues = {queueName})
    public void onEsimNotificationRequest(Message message) {
        try {
            this.eSimNotificationRequest(message);
        } catch (Exception e) {
            throw new EqmMessagingException(e.getMessage());
        }
    }

    private void eSimNotificationRequest(Message message) {
        byte[] messageByte = message.getBody();
        try {
            var esimNotification = objectMapper.readValue(messageByte, EsimNotificationIngress.class);
            log.info(String.format("ESIM-NOTIFICATION: received esimNotification with iccid: %s", esimNotification.getIccid()));
            esimNotificationHandler.handlePersist(esimNotification);
        } catch (IOException e) {
            throw new EqmMessagingException(e.getMessage());
        }
    }
}

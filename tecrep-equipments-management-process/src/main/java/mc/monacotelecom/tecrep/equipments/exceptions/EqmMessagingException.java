package mc.monacotelecom.tecrep.equipments.exceptions;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;

public class EqmMessagingException extends AmqpRejectAndDontRequeueException {
    public EqmMessagingException(String message) {
        super(message);
    }

    public EqmMessagingException(String message, Throwable e) {
        super(message, e);
    }
}

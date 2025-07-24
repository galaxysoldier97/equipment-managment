package mc.monacotelecom.tecrep.equipments.exceptions;

import mc.monacotelecom.inventory.common.nls.LocalizedException;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;

public class EqmInternalException extends LocalizedException {
    public EqmInternalException(LocalizedMessageBuilder messageBuilder, String messageKey, Object... args) {
        super(messageBuilder, messageKey, args);
    }

    public EqmInternalException(Throwable cause, LocalizedMessageBuilder messageBuilder, String messageKey, Object... args) {
        super(cause, messageBuilder, messageKey, args);
    }
}

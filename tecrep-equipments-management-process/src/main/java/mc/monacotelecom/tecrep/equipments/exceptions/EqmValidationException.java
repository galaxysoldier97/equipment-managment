package mc.monacotelecom.tecrep.equipments.exceptions;

import mc.monacotelecom.inventory.common.nls.LocalizedException;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;

public class EqmValidationException extends LocalizedException {
    public EqmValidationException(LocalizedMessageBuilder messageBuilder, String messageKey, Object... args) {
        super(messageBuilder, messageKey, args);
    }

    public EqmValidationException(Throwable e, LocalizedMessageBuilder messageBuilder, String messageKey, Object... args) {
        super(e, messageBuilder, messageKey, args);
    }
}

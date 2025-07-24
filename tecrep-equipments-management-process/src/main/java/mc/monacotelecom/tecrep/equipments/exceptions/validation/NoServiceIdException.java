package mc.monacotelecom.tecrep.equipments.exceptions.validation;

import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;

@SuppressWarnings("java:S110")
public class NoServiceIdException extends EqmValidationException {
    public NoServiceIdException(LocalizedMessageBuilder messageBuilder, String messageKey, Object... args) {
        super(messageBuilder, messageKey, args);
    }
}

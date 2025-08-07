package mc.monacotelecom.tecrep.equipments.exceptions;

import mc.monacotelecom.inventory.common.nls.LocalizedException;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class EqmValidationException extends LocalizedException {
    public EqmValidationException(LocalizedMessageBuilder messageBuilder, String messageKey, Object... args) {
        super(messageBuilder, messageKey, args);
    }

    public EqmValidationException(Throwable e, LocalizedMessageBuilder messageBuilder, String messageKey, Object... args) {
        super(e, messageBuilder, messageKey, args);
    }

    @Override
    public String getMessage() {
        Object[] args = getArgs();
        if (args != null && args.length > 0 && args[0] != null) {
            return args[0].toString();
        }
        return getMessageKey();
    }
}

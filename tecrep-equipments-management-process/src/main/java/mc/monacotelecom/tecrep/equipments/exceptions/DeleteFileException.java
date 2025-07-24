package mc.monacotelecom.tecrep.equipments.exceptions;

import mc.monacotelecom.inventory.common.nls.LocalizedException;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;

public class DeleteFileException extends LocalizedException {

    public DeleteFileException(LocalizedMessageBuilder messageBuilder, String messageKey, Object... args) { super(messageBuilder, messageKey, args); }
}

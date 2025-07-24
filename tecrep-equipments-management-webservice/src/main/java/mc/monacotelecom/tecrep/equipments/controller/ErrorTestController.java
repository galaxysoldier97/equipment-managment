package mc.monacotelecom.tecrep.equipments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmConflictException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmInternalException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmNotFoundException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/error-test")
@RequiredArgsConstructor
public class ErrorTestController {
    
    private final LocalizedMessageBuilder localizedMessageBuilder;

    @GetMapping("/not-found/{message}")
    public void testNotFoundException(@PathVariable String message) {
        log.debug("Testing not found exception with message key: {}", message);
        throw new EqmNotFoundException(localizedMessageBuilder, message);
    }

    @GetMapping("/validation/{message}")
    public void testValidationException(@PathVariable String message) {
        log.debug("Testing validation exception with message key: {}", message);
        throw new EqmValidationException(localizedMessageBuilder, message);
    }
    
    @GetMapping("/conflict/{message}")
    public void testConflictException(@PathVariable String message) {
        log.debug("Testing conflict exception with message key: {}", message);
        throw new EqmConflictException(localizedMessageBuilder, message);
    }
    
    @GetMapping("/internal/{message}")
    public void testInternalException(@PathVariable String message) {
        log.debug("Testing internal exception with message key: {}", message);
        throw new EqmInternalException(localizedMessageBuilder, message);
    }
    
    @GetMapping("/runtime/{message}")
    public void testRuntimeException(@PathVariable String message) {
        log.debug("Testing runtime exception with message: {}", message);
        throw new RuntimeException(message);
    }
    
    @GetMapping("/localized/{messageKey}")
    public void testLocalizedMessage(@PathVariable String messageKey) {
        log.debug("Testing localized message with key: {}", messageKey);
        try {
            // Para localizar mensajes, simplemente pasamos la clave y los argumentos al constructor de la excepción
            throw new EqmNotFoundException(localizedMessageBuilder, messageKey, "test-id");
        } catch (Exception e) {
            log.error("Error creating exception: ", e);
            throw new RuntimeException("Error al crear excepción con clave: " + messageKey);
        }
    }
}
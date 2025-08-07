package mc.monacotelecom.tecrep.equipments.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.exporter.exception.CommonExporterException;

import mc.monacotelecom.inventory.common.importer.controller.ImporterController;
import mc.monacotelecom.inventory.common.importer.exceptions.CommonImportNotFoundException;
import mc.monacotelecom.inventory.common.importer.exceptions.CommonImportValidationException;
import mc.monacotelecom.inventory.common.nls.LocalizedException;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.inventory.job.web.controler.JobConfigurationController;
import mc.monacotelecom.inventory.job.web.controler.JobController;
import mc.monacotelecom.inventory.common.recycling.exceptions.JobNotFoundException;
import mc.monacotelecom.tecrep.equipments.controller.*;
import mc.monacotelecom.tecrep.equipments.dto.CustomErrorResponse;
import mc.monacotelecom.tecrep.equipments.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Locale;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.FIELD_VALIDATION_ERROR;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice(assignableTypes = {
        CPEControllerV2.class,
        AncillaryControllerV2.class,
        SimCardControllerV2.class,
        EquipmentModelControllerV2.class,
        PlmnControllerV2.class,
        ProviderControllerV2.class,
        SimCardGenerationConfigurationControllerV2.class,
        WarehouseControllerV2.class,
        EquipmentTempController.class
})
@RequiredArgsConstructor
public class EqmControllerAdviceV1 {

    private static final String EXCEPTION_MESSAGE = "Exception : ";
    private final Clock clock;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler({RuntimeException.class, NullPointerException.class})
    public CustomErrorResponse handleRuntimeException(Exception e, Locale locale) {
        log.error(EXCEPTION_MESSAGE, e);
        return error(INTERNAL_SERVER_ERROR, e, resolveMessage(e, locale));
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler({EqmNotFoundException.class, CommonImportNotFoundException.class, JobNotFoundException.class})
    public CustomErrorResponse handleNotFoundException(Exception e, Locale locale) {
        log.debug(EXCEPTION_MESSAGE, e);
        return error(NOT_FOUND, e, resolveMessage(e, locale));
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler({EqmValidationException.class, CommonImportValidationException.class, CommonExporterException.class})
    public CustomErrorResponse handleUnprocessableEntityException(Exception e, Locale locale) {
        log.debug(EXCEPTION_MESSAGE, e);
        return error(UNPROCESSABLE_ENTITY, e, resolveMessage(e, locale));
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public CustomErrorResponse handleConstraintViolationException(ConstraintViolationException e, Locale locale) {
        log.error(EXCEPTION_MESSAGE, e);
        return error(BAD_REQUEST, e, resolveMessage(e, locale));
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CustomErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e, Locale locale) {
        var fieldError = e.getBindingResult().getFieldErrors().get(0);
        String message = localizedMessageBuilder.getLocalizedMessage(
                FIELD_VALIDATION_ERROR,
                ObjectUtils.nullSafeToString(fieldError.getRejectedValue()),
                fieldError.getField()
        );
        log.error(EXCEPTION_MESSAGE, e);
        return error(BAD_REQUEST, e, message != null ? message : "Unknown validation error");
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(EqmInternalException.class)
    public CustomErrorResponse handleInternalException(Exception e, Locale locale) {
        log.debug(EXCEPTION_MESSAGE, e);
        return error(INTERNAL_SERVER_ERROR, e, resolveMessage(e, locale));
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(EqmConflictException.class)
    public CustomErrorResponse handleConflictException(Exception e, Locale locale) {
        log.debug(EXCEPTION_MESSAGE, e);
        return error(CONFLICT, e, resolveMessage(e, locale));
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public CustomErrorResponse handleAny(Exception e, Locale locale) {
        log.error("💥 Excepción no capturada:", e);
        String message;
        if (e instanceof LocalizedException) {
            LocalizedException localized = (LocalizedException) e;
            message = localizedMessageBuilder.getLocalizedMessage(localized.getMessageKey(), localized.getArgs());
        } else {
            message = (e.getMessage() != null ? e.getMessage() : "Unknown internal error");
        }
        return error(INTERNAL_SERVER_ERROR, e, message);
    }

    private String resolveMessage(Exception e, Locale locale) {
        if (e instanceof EqmInternalException && e.getCause() != null && e.getCause().getMessage() != null) {
            return e.getCause().getMessage();
        }
        if (e instanceof LocalizedException) {
            LocalizedException le = (LocalizedException) e;
            String key = le.getMessageKey();
            Object[] args = le.getArgs();
            log.info("🌍 Traduciendo clave '{}' con args {} y locale {}", key, args, locale);
            String localized = localizedMessageBuilder.getLocalizedMessage(key, args);
            if ((localized == null || localized.equals(key)) && args != null && args.length > 0 && args[0] != null) {
                return args[0].toString();
            }
            return localized;
        }
        return e.getMessage() != null ? e.getMessage() : "Unknown error";
    }

    // Método de sobrecarga con 2 parámetros
    private CustomErrorResponse error(HttpStatus status, String message) {
        return error(status, null, message);
    }

    // Método principal con 3 parámetros
    private CustomErrorResponse error(HttpStatus status, Exception e, String message) {
        if (e != null) {
            //log.error("Exception : ", e);
            if (status == HttpStatus.NOT_FOUND) {
                log.debug("Exception : ", e);
            } else {
                log.error("Exception : ", e);
            }
        }
        
        // Registra el mensaje para depuración
        log.debug("Error message: '{}'", message);
        
        var customErrorResponse = new CustomErrorResponse();
        customErrorResponse.setTimestamp(LocalDateTime.now(clock));
        customErrorResponse.setStatus(status.value());
        
        // El error contiene el HTTP status
        customErrorResponse.setError(status.getReasonPhrase());
        
        // Configurar el mensaje específico de la excepción
        customErrorResponse.setMessage(message != null ? message : "Ocurrió un error inesperado");
        
        return customErrorResponse;
    }
    
}
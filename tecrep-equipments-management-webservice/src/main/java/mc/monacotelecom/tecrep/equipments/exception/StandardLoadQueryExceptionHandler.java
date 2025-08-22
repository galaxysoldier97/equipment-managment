package mc.monacotelecom.tecrep.equipments.exception;

import mc.monacotelecom.tecrep.equipments.controller.StandardLoadQueryController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(assignableTypes = StandardLoadQueryController.class)
public class StandardLoadQueryExceptionHandler {

    @ExceptionHandler(StandardLoadNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleNotFound(StandardLoadNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("idStandard", ex.getIdStandard());
        return body;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleGeneric(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "internal server error");
        return body;
    }
}

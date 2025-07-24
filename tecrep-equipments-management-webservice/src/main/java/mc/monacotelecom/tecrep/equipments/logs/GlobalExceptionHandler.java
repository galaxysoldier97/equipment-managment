package mc.monacotelecom.tecrep.equipments.logs;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationErrors(MethodArgumentNotValidException ex,
                                                         HttpServletRequest request) {

        String uri = request.getRequestURI();
        String processName = uri.replaceFirst("^/(private/auth/|public/)", "").split("\\?")[0];

        String trace_id = request.getHeader("X-Trace-ID");
        String request_id = request.getHeader("X-Request-ID");
        String correlation_id = request.getHeader("X-Correlation-ID");

        trace_id = (trace_id == null || trace_id.isBlank()) ? "N/A" : trace_id;
        request_id = (request_id == null || request_id.isBlank()) ? "N/A" : request_id;
        correlation_id = (correlation_id == null || correlation_id.isBlank()) ? "N/A" : correlation_id;

        String uti = String.format("trace_id: %s | request_id: %s | correlation_id: %s",
                trace_id, request_id, correlation_id);

        MDC.put("processName", processName);
        MDC.put("uti", uti);

        String errorMsg = ex.getBindingResult().getAllErrors().stream()
                .map(e -> e.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b).orElse("Unknown validation error");

        log.error("400 ERROR => process: {}, error: {}", processName, errorMsg);

        MDC.clear();

        return ResponseEntity.badRequest().body("Validation failed: " + errorMsg);
    }
}

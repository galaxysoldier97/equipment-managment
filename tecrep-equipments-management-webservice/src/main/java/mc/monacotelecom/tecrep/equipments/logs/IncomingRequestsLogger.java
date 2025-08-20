package mc.monacotelecom.tecrep.equipments.logs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.slf4j.MDC;

import static mc.monacotelecom.tecrep.equipments.logs.LogUtils.getUserName;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Aspect
@Configuration
@RequiredArgsConstructor
@Slf4j
public class IncomingRequestsLogger {

    private final HttpServletRequest httpRequest;
    private final HttpServletResponse httpResponse;

    // ObjectMapper preparado para soportar LocalDateTime
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Around("execution(* mc.monacotelecom.tecrep.equipments.controller..*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String processName = httpRequest.getRequestURI();
        //String cleanedUri = rawUri.replaceFirst("^/(private/auth/|public/)", "");
        //String processName = cleanedUri.split("\\?")[0];

        // Headers
        String trace_id = httpRequest.getHeader("X-Trace-ID");
        String request_id = httpRequest.getHeader("X-Request-ID");
        String correlation_id = httpRequest.getHeader("X-Correlation-ID");

        trace_id = (trace_id == null || trace_id.isBlank()) ? "N/A" : trace_id;
        request_id = (request_id == null || request_id.isBlank()) ? "N/A" : request_id;
        correlation_id = (correlation_id == null || correlation_id.isBlank()) ? "N/A" : correlation_id;

        String uti = String.format("trace_id: %s | request_id: %s | correlation_id: %s",
                trace_id, request_id, correlation_id);

        MDC.put("processName", processName);
        MDC.put("uti", uti);
        MDC.put("user", getUserName());

        long startTime = System.currentTimeMillis();

        String parameters = Arrays.stream(joinPoint.getArgs())
                .filter(arg -> arg != null && !(arg instanceof HttpServletRequest))
                .map(arg -> {
                    try {
                        return objectMapper.writeValueAsString(arg);
                    } catch (JsonProcessingException e) {
                        return arg.toString();
                    }
                })
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            int status = httpResponse.getStatus();

            String responseBody = (result != null) ? objectMapper.writeValueAsString(result) : "null";

            log.info("OK => process: {}, status: {}, durationMs: {}, parameters: {}, response: {}",
                    processName, status, duration, parameters, responseBody);

            return result;

        } catch (Throwable ex) {
            long duration = System.currentTimeMillis() - startTime;

            int status = 500;
            String simpleError = ex.getMessage();

            // Manejo especial para errores de validación 400
            if (ex instanceof org.springframework.web.bind.MethodArgumentNotValidException) {
                status = 400;
                simpleError = "[MethodArgumentNotValidException] - " + ex.getMessage();
            }

            log.error("ERROR => process: {}, status: {}, durationMs: {}, error: {}, parameters: {}",
                    processName, status, duration, simpleError, parameters);


            throw ex;
        } finally {
            MDC.clear();
        }
    }
}

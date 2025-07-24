package mc.monacotelecom.tecrep.equipments.logs;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        long startTime = System.currentTimeMillis();
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

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            int status = response.getStatus();

            log.info("TRACE => process: {}, status: {}, duration: {} ms", processName, status, duration);
            MDC.clear();
        }
    }
}

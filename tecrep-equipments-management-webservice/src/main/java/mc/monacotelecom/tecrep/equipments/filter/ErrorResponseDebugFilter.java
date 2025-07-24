package mc.monacotelecom.tecrep.equipments.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@Order(Ordered.LOWEST_PRECEDENCE) // Asegura que este filtro se ejecute al final
public class ErrorResponseDebugFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        
        try {
            filterChain.doFilter(request, responseWrapper);
        } finally {
            // Para respuestas de error (4xx y 5xx)
            if (responseWrapper.getStatus() >= 400) {
                byte[] responseBody = responseWrapper.getContentAsByteArray();
                if (responseBody.length > 0) {
                    String responseContent = new String(responseBody, StandardCharsets.UTF_8);
                    log.debug("ERROR RESPONSE - Status: {}, Body: {}", 
                             responseWrapper.getStatus(), responseContent);
                    
                    // También imprime todas las cabeceras
                    log.debug("ERROR RESPONSE - Headers:");
                    for (String headerName : responseWrapper.getHeaderNames()) {
                        log.debug("  {}: {}", headerName, responseWrapper.getHeader(headerName));
                    }
                } else {
                    log.debug("ERROR RESPONSE - Status: {}, Body is empty", 
                             responseWrapper.getStatus());
                }
            }
            
            responseWrapper.copyBodyToResponse();
        }
    }
}
package mc.monacotelecom.tecrep.equipments.logs;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility class for logging related helpers.
 */
public final class LogUtils {

    private LogUtils() {
    }

    /**
     * Extracts the user name from the current security context.
     *
     * @return the user name if available, otherwise "N/A".
     */
    public static String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof KeycloakPrincipal<?>) {
                AccessToken token = ((KeycloakPrincipal<?>) principal)
                        .getKeycloakSecurityContext().getToken();
                if (token != null && token.getName() != null && !token.getName().isBlank()) {
                    return token.getName();
                }
            }
            String name = authentication.getName();
            if (name != null && !name.isBlank()) {
                return name;
            }
        }
        return "N/A";
    }
}

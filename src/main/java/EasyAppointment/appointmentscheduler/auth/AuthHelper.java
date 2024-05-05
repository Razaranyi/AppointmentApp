package EasyAppointment.appointmentscheduler.auth;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * This is a helper class for authentication operations.
 * It provides methods to get the name of the authenticated user.
 */
public class AuthHelper {

    /**
     * This method retrieves the name of the authenticated user.
     * It uses the SecurityContextHolder to get the authentication object and then gets the name from it.
     * @return The name of the authenticated user.
     */
    public static String getCaller(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
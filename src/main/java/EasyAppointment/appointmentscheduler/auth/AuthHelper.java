package EasyAppointment.appointmentscheduler.auth;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthHelper {

    public static String getCaller(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}

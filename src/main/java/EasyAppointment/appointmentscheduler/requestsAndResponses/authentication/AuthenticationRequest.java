package EasyAppointment.appointmentscheduler.requestsAndResponses.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents the authentication request.
 * It contains the email and password for authentication.
 * It uses Lombok annotations for automatic generation of getters, setters, constructors, and builder.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    /**
     * The email used for authentication.
     */
    private String email;

    /**
     * The password used for authentication.
     */
    private String password;
}
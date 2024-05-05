package EasyAppointment.appointmentscheduler.requestsAndResponses.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents the authentication response.
 * It contains the token and message after successful authentication.
 * It uses Lombok annotations for automatic generation of getters, setters, constructors, and builder.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    /**
     * The token received after successful authentication.
     */
    private String token;

    /**
     * The message received after successful authentication.
     */
    private String message;

    /**
     * Constructor that initializes the token.
     * @param token The token received after successful authentication.
     */
    public AuthenticationResponse(String token) {
        this.token = token;
    }
}
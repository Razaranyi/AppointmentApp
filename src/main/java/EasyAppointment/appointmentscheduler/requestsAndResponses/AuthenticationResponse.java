package EasyAppointment.appointmentscheduler.requestsAndResponses;

import EasyAppointment.appointmentscheduler.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.connector.Response;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
    private String message;
    public AuthenticationResponse(String token) {
        this.token = token;
    }

}



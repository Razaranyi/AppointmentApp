package EasyAppointment.appointmentscheduler.auth;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Full name is required")
    @Pattern(regexp = "^[\\u0590-\\u05FF\\uFB1D-\\uFB4F A-Za-z-\\s']+$", message = "Name must contain only valid characters")
    private String fullName;
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z0-9.-]+$", message = "Invalid email format")
    private String email;
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}

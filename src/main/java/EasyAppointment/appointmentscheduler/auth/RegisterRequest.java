package EasyAppointment.appointmentscheduler.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Email(message = "Invalid email format")
    private String email;
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}

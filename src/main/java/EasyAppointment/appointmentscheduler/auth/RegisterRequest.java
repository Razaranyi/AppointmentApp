package EasyAppointment.appointmentscheduler.auth;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is a data class for the registration request.
 * It contains the full name, email, and password of the user to be registered.
 * It uses Lombok annotations for automatic generation of getters, setters, constructors, and builder.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    /**
     * The full name of the user.
     * It must not be blank and must contain only valid characters for a standard name. full name is not compelled.
     */
    @NotBlank(message = "Full name is required")
    @Pattern(regexp = "^[\\u0590-\\u05FF\\uFB1D-\\uFB4F A-Za-z-\\s']+$", message = "Name must contain only valid characters")
    private String fullName;

    /**
     * The email of the user.
     * It must be in a valid email format.
     */
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z0-9.-]+$", message = "Invalid email format")
    private String email;

    /**
     * The password of the user.
     * It must be at least 8 characters long.
     */
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
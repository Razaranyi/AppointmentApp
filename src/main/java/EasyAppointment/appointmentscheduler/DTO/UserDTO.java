package EasyAppointment.appointmentscheduler.DTO;

import EasyAppointment.appointmentscheduler.models.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is a data transfer object (DTO) for User.
 * It is used to send data over the network or between processes.
 * It includes all the necessary information about a user.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements DTOInterface {
    /**
     * The ID of the user.
     */
    private Long id;

    /**
     * The full name of the user.
     * It must not be blank and must contain only valid characters.
     */
    @NotBlank(message = "Full name is required")
    @Pattern(regexp = "^[\\u0590-\\u05FF\\uFB1D-\\uFB4F A-Za-z-\\s']+$", message = "Name must contain only valid characters")
    private String fullName;

    /**
     * The email of the user.
     * It must not be blank and must be in a valid email format.
     */
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Invalid email format")
    private String email;

    /**
     * The password of the user.
     * It must be at least 8 characters long.
     */
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    /**
     * This constructor is used to create a UserDTO from a User object.
     * It copies all the necessary information from the User object to the UserDTO.
     * @param user The User object to be converted into a UserDTO.
     */
    public UserDTO(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
    }
}
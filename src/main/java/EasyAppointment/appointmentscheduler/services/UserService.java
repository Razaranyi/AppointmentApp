package EasyAppointment.appointmentscheduler.services;


import EasyAppointment.appointmentscheduler.models.Role;
import EasyAppointment.appointmentscheduler.models.User;
import EasyAppointment.appointmentscheduler.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * This class provides services related to users.
 * It uses Spring's @Service annotation to indicate that it's a service class.
 * It uses Lombok's @RequiredArgsConstructor to automatically generate a constructor with required fields.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    /**
     * This method retrieves the role of the currently authenticated user.
     * @param authentication The authentication object containing the user's details.
     * @return The Role object associated with the authenticated user.
     */
    public Role getAuthenticatedUserRole(Authentication authentication) {
        String userEmail = authentication.getName();
        return userRepository.findRoleByEmail(userEmail);
    }

    /**
     * This method updates the role of a user.
     * @param email The email of the user whose role is to be updated.
     * @param role The new role to be assigned to the user.
     * @throws UsernameNotFoundException if the user is not found.
     */
    public void updateUserRole(String email, Role role) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        user.setRole(role);
        userRepository.save(user);
    }
}
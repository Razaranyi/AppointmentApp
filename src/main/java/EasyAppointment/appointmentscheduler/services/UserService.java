package EasyAppointment.appointmentscheduler.services;


import EasyAppointment.appointmentscheduler.models.Role;
import EasyAppointment.appointmentscheduler.models.User;
import EasyAppointment.appointmentscheduler.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Role getAuthenticatedUserRole(Authentication authentication) {
        String userEmail = authentication.getName();
        return userRepository.findRoleByEmail(userEmail);
    }

    public void updateUserRole(String email, Role role) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        user.setRole(role);
        userRepository.save(user);
    }
}
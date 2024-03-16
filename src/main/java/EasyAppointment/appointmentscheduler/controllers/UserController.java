package EasyAppointment.appointmentscheduler.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/profile")
    public String getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ""; // Initialize or declare the userId variable

        if (authentication != null && authentication.getPrincipal() != null) {
            // Check if the principal is an instance of UserDetails or a String (username or userID)
            // This depends on your JWTAuthenticationFilter's implementation
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                userId = ((UserDetails)principal).getUsername(); // Assuming getUsername() returns the userID
            } else if (principal instanceof String) {
                userId = (String) principal; // Directly use it if it's a string
            }

            // Now, userId should have the user's ID, which you can use to fetch user-specific data
        }

        // Use the userId to perform operations, like fetching user profile, etc.
        return "User ID: " + userId;
    }
}

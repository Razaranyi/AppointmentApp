package EasyAppointment.appointmentscheduler.auth;

import EasyAppointment.appointmentscheduler.config.JwtService;
import EasyAppointment.appointmentscheduler.exception.UserAlreadyExistException;
import EasyAppointment.appointmentscheduler.models.Role;
import EasyAppointment.appointmentscheduler.models.User;
import EasyAppointment.appointmentscheduler.repositories.UserRepository;
import EasyAppointment.appointmentscheduler.requestsAndResponses.authentication.AuthenticationRequest;
import EasyAppointment.appointmentscheduler.requestsAndResponses.authentication.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * This is the service for authentication operations.
 * It handles user registration and authentication.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * This method handles the registration of a new user.
     * It checks if a user with the same email already exists, creates a new user, and generates a JWT for the user.
     * @param request The registration request.
     * @return AuthenticationResponse containing the JWT and a success message.
     * @throws UserAlreadyExistException if a user with the same email already exists.
     */
    public AuthenticationResponse registerUser(RegisterRequest request) throws UserAlreadyExistException {
       if (userRepository.findByEmail(request.getEmail().toLowerCase()).isPresent()) {
            throw new UserAlreadyExistException("User with email " + request.getEmail() + " already exists");
        }
        var user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var twtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(twtToken)
                .message("User registered successfully")
                .build();
    }

    /**
     * This method handles the authentication of a user.
     * It checks the user's credentials, retrieves the user's details, and generates a JWT for the user.
     * @param request The authentication request.
     * @return AuthenticationResponse containing the JWT and a success message.
     * @throws UsernameNotFoundException if the user's credentials are invalid or the user is not found.
     */
    public AuthenticationResponse authenticateUser(AuthenticationRequest request) {

        try{  authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail().toLowerCase(),
                        request.getPassword()));
        }

        catch (Exception WrongCredentialsException){
           throw new UsernameNotFoundException("Invalid credentials");
        }


        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                new UsernameNotFoundException("User " + request.getEmail().toLowerCase() + " not found"));

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .message("User authenticated successfully")
                .build();
    }
}
package EasyAppointment.appointmentscheduler.auth;

import EasyAppointment.appointmentscheduler.config.JwtService;
import EasyAppointment.appointmentscheduler.exception.UserAlreadyExistException;
import EasyAppointment.appointmentscheduler.models.Role;
import EasyAppointment.appointmentscheduler.models.User;
import EasyAppointment.appointmentscheduler.repositories.UserRepository;
import EasyAppointment.appointmentscheduler.requestsAndResponses.AuthenticationRequest;
import EasyAppointment.appointmentscheduler.requestsAndResponses.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
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

    public AuthenticationResponse authenticateUser(AuthenticationRequest request) {

        try{  authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail().toLowerCase(),
                        request.getPassword()));
        }

        catch (Exception WrongCredentialsException){
            return AuthenticationResponse.builder()
                    .message("Invalid email or password")
                    .build();
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

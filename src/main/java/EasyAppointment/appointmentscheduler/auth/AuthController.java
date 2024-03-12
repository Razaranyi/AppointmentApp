package EasyAppointment.appointmentscheduler.auth;

import EasyAppointment.appointmentscheduler.exception.UserAlreadyExistException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authService;
    @PostMapping("/sign-up")
    public ResponseEntity<AuthenticationResponse> registerUser(
            @Valid @RequestBody RegisterRequest request, BindingResult result){
        if (result.hasErrors()) {
            String errorMessages = result.getAllErrors() // Collect all validation errors
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage) // Extract default messages
                    .collect(Collectors.joining(", ")); // Join them with comma separation
            return ResponseEntity.badRequest().body(
                    AuthenticationResponse.builder()
                            .message(errorMessages)
                            .build()
            );
        }
        try {
            return ResponseEntity.ok(authService.registerUser(request));
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.badRequest().body(
                    AuthenticationResponse.builder()
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateUser(
            @RequestBody AuthenticationRequest request) {

        return ResponseEntity.ok(
                authService.authenticateUser(request));
    }


}



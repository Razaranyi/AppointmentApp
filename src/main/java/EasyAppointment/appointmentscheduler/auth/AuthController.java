package EasyAppointment.appointmentscheduler.auth;

import EasyAppointment.appointmentscheduler.exception.UserAlreadyExistException;
import EasyAppointment.appointmentscheduler.requestsAndResponses.authentication.AuthenticationRequest;
import EasyAppointment.appointmentscheduler.requestsAndResponses.authentication.AuthenticationResponse;
import EasyAppointment.appointmentscheduler.util.ControllerUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authService;
    @PostMapping("/sign-up")
    public ResponseEntity<AuthenticationResponse> registerUser(
            @Valid @RequestBody RegisterRequest request, BindingResult result) throws UserAlreadyExistException {
        if (result.hasErrors()) {
            String errorMessages = ControllerUtils.getErrorMessages(result);
            return ResponseEntity.badRequest().body(
                    AuthenticationResponse.builder()
                            .message(errorMessages)
                            .build()
            );
        }
        return ResponseEntity.ok(authService.registerUser(request));

    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateUser(
            @RequestBody AuthenticationRequest request) {

        return ResponseEntity.ok(
                authService.authenticateUser(request));
    }


}



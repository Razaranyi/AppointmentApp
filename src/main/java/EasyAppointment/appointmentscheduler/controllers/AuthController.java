package EasyAppointment.appointmentscheduler.controllers;

import EasyAppointment.appointmentscheduler.DTO.UserDTO;
import EasyAppointment.appointmentscheduler.exception.UserAlreadyExistException;
import EasyAppointment.appointmentscheduler.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessages = result.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            System.out.println(errorMessages);
            return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
        }
        try {
            userService.registerNewUser(userDTO);
            return new ResponseEntity<String >("User registered successfully", HttpStatus.OK);
        } catch (UserAlreadyExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}



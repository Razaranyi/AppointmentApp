package EasyAppointment.appointmentscheduler.controllers;

import EasyAppointment.appointmentscheduler.models.Business;
import EasyAppointment.appointmentscheduler.services.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/businesses")
@RequiredArgsConstructor
public class BusinessController {
    private final BusinessService businessService;

    @GetMapping("/my-business")
    public ResponseEntity<?> getMyBusiness(Authentication authentication) {
        String authenticatedUserEmail = authentication.getName(); // Gets the email from the current authentication principal
        Optional<Business> businessOptional = businessService.getBusinessByEmail(authenticatedUserEmail);

        return businessOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

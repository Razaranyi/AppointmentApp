package EasyAppointment.appointmentscheduler.controllers;

import EasyAppointment.appointmentscheduler.repositories.UserRepository;
import EasyAppointment.appointmentscheduler.requestsAndResponses.BusinessCreatedResponse;
import EasyAppointment.appointmentscheduler.requestsAndResponses.BusinessCreationRequest;
import EasyAppointment.appointmentscheduler.exception.UserAlreadyOwnsBusinessException;
import EasyAppointment.appointmentscheduler.models.Business;
import EasyAppointment.appointmentscheduler.services.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<BusinessCreatedResponse> createBusiness(@RequestBody BusinessCreationRequest request, Authentication authentication){
        try{
            String userEmail = authentication.getName(); // Gets the email from the current authentication principal
            return ResponseEntity.ok(businessService.createBusiness(request, userEmail));
        } catch (UserAlreadyOwnsBusinessException | UsernameNotFoundException e ) {
            return ResponseEntity.badRequest().body(
                    BusinessCreatedResponse.builder()
                            .message(e.getMessage())
                            .build());
        }
    }
}

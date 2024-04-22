package EasyAppointment.appointmentscheduler.controllers;

import EasyAppointment.appointmentscheduler.DTO.BusinessDTO;
import EasyAppointment.appointmentscheduler.models.Business;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiRequest;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiResponse;
import EasyAppointment.appointmentscheduler.services.BusinessService;
import EasyAppointment.appointmentscheduler.util.ControllerUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/businesses")
@RequiredArgsConstructor
public class BusinessController {
    private final BusinessService businessService;

    @GetMapping("/my-business")// fix this method to return all relevant business data
    public ResponseEntity<BusinessDTO> getMyBusiness(Authentication authentication) {
        String authenticatedUserEmail = authentication.getName(); // Gets the email from the current authentication principal
        Optional<Business> businessOptional = businessService.getBusinessByEmail(authenticatedUserEmail);

        return businessOptional
                .map(business -> ResponseEntity.ok(new BusinessDTO(business)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<BusinessDTO>> createBusiness(
            @Valid @RequestBody ApiRequest<BusinessDTO> request,
            BindingResult result) {
        // Check if the request has validation errors
        if (result.hasErrors()) {
            String errorMessages = ControllerUtils.getErrorMessages(result);
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, errorMessages, null));
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return ResponseEntity.ok(businessService.addBusiness(request, userEmail));
    }

    @PostMapping("/business/{id}/logo")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?> uploadBusinessLogo(@PathVariable Long id, @RequestParam("logo") MultipartFile logo) throws IOException {

        return ResponseEntity.ok(businessService.addLogoToBusiness(id, logo.getBytes()));
    }

}

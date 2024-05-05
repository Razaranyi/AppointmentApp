package EasyAppointment.appointmentscheduler.controllers;

import EasyAppointment.appointmentscheduler.DTO.BusinessDTO;
import EasyAppointment.appointmentscheduler.auth.AuthHelper;
import EasyAppointment.appointmentscheduler.models.Business;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiRequest;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiResponse;
import EasyAppointment.appointmentscheduler.services.BusinessService;
import EasyAppointment.appointmentscheduler.util.ControllerUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/business")
@RequiredArgsConstructor
public class BusinessController {
    private final BusinessService businessService;

    @GetMapping("/my-business")
    public ResponseEntity<ApiResponse<BusinessDTO>> getMyBusiness() {

        String authenticatedUserEmail = AuthHelper.getCaller();
        return ResponseEntity.ok(businessService.getBusinessByEmail(authenticatedUserEmail));
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
        String userEmail = AuthHelper.getCaller();
        return ResponseEntity.ok(businessService.addBusiness(request, userEmail));
    }


    @GetMapping("/get-id")
    public ResponseEntity<ApiResponse<BusinessDTO>> getBusinessId() {
        return ResponseEntity.ok(businessService.getBusinessId());
    }

    @GetMapping("/get-business-by-id/{id}")
    public ResponseEntity<ApiResponse<BusinessDTO>> getBusinessById(@PathVariable Long id) {
        return ResponseEntity.ok(businessService.getBusinessById(id));
    }
}

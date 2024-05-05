package EasyAppointment.appointmentscheduler.controllers;

import EasyAppointment.appointmentscheduler.DTO.BusinessDTO;
import EasyAppointment.appointmentscheduler.auth.AuthHelper;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiRequest;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiResponse;
import EasyAppointment.appointmentscheduler.services.BusinessService;
import EasyAppointment.appointmentscheduler.util.ControllerUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * This is the controller for the Business entity.
 * It handles HTTP requests and responses related to Business operations.
 */
@RestController
@RequestMapping("/api/business")
@RequiredArgsConstructor
public class BusinessController {
    private final BusinessService businessService;

    /**
     * This method handles the GET request to retrieve the business details of the authenticated user.
     * @return ResponseEntity containing ApiResponse with BusinessDTO
     */
    @GetMapping("/my-business")
    public ResponseEntity<ApiResponse<BusinessDTO>> getMyBusiness() {

        String authenticatedUserEmail = AuthHelper.getCaller();
        return ResponseEntity.ok(businessService.getBusinessByEmail(authenticatedUserEmail));
    }

    /**
     * This method handles the POST request to create a new business.
     * @param request The request body containing the business details to be created.
     * @param result BindingResult object to hold validation results.
     * @return ResponseEntity containing ApiResponse with BusinessDTO
     */
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

    /**
     * This method handles the GET request to retrieve the business ID.
     * @return ResponseEntity containing ApiResponse with BusinessDTO
     */
    @GetMapping("/get-id")
    public ResponseEntity<ApiResponse<BusinessDTO>> getBusinessId() {
        return ResponseEntity.ok(businessService.getBusinessId());
    }

    /**
     * This method handles the GET request to retrieve the business details by ID.
     * @param id The ID of the business to be retrieved.
     * @return ResponseEntity containing ApiResponse with BusinessDTO
     */
    @GetMapping("/get-business-by-id/{id}")
    public ResponseEntity<ApiResponse<BusinessDTO>> getBusinessById(@PathVariable Long id) {
        return ResponseEntity.ok(businessService.getBusinessById(id));
    }
}
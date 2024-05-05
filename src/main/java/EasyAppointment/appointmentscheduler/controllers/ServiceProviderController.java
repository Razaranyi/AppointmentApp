package EasyAppointment.appointmentscheduler.controllers;

import EasyAppointment.appointmentscheduler.DTO.ServiceProviderDTO;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiRequest;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiResponse;
import EasyAppointment.appointmentscheduler.services.ServiceProviderService;
import EasyAppointment.appointmentscheduler.util.ControllerUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This is the controller for the ServiceProvider entity.
 * It handles HTTP requests and responses related to ServiceProvider operations.
 */
@RestController
@RequestMapping("/api/business/{businessId}/{branchId}/service-provider")
@RequiredArgsConstructor
public class ServiceProviderController {
    private final ServiceProviderService serviceProviderService;

    /**
     * This method handles the GET request to retrieve all service providers by the authenticated business owner.
     * @param businessId The ID of the business.
     * @param branchId The ID of the branch.
     * @return ResponseEntity containing ApiResponse with List of ServiceProviderDTO
     */
    @GetMapping("/get-all")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<ServiceProviderDTO>>> getBranchesByAuthenticatedBusinessOwner(
            @PathVariable Long businessId,
            @PathVariable Long branchId) {

            return ResponseEntity.ok(serviceProviderService.getServiceProviderListByBranch(branchId,businessId));
    }

    /**
     * This method handles the GET request to retrieve a service provider by ID.
     * @param branchId The ID of the branch.
     * @param businessId The ID of the business.
     * @param serviceProviderId The ID of the service provider.
     * @return ResponseEntity containing ApiResponse with ServiceProviderDTO
     */
    @GetMapping("/{serviceProviderId}/get")
    public ResponseEntity<ApiResponse<ServiceProviderDTO>> getServiceProviderById(
            @PathVariable Long branchId,
            @PathVariable Long businessId,
            @PathVariable Long serviceProviderId) {

            return ResponseEntity.ok(serviceProviderService.getServiceProvidersById(branchId, serviceProviderId,businessId));
    }

    /**
     * This method handles the POST request to create a new service provider.
     * @param request The request body containing the service provider details to be created.
     * @param authentication The authentication object.
     * @param branchId The ID of the branch.
     * @param result BindingResult object to hold validation results.
     * @return ResponseEntity containing ApiResponse with ServiceProviderDTO
     */
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<ServiceProviderDTO>> createServiceProvider(@Valid
            @RequestBody ApiRequest<ServiceProviderDTO> request,
            Authentication authentication,
            @PathVariable Long branchId,
            BindingResult result) {

        if (result.hasErrors()) {
            String errorMessages = ControllerUtils.getErrorMessages(result);
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, errorMessages, null));
        }

            return ResponseEntity.ok(serviceProviderService.addServiceProvider(branchId, request, authentication.getName()));
    }

    /**
     * This method handles the DELETE request to delete a service provider.
     * @param branchId The ID of the branch.
     * @param authentication The authentication object.
     * @param serviceProviderId The ID of the service provider.
     * @return ResponseEntity containing ApiResponse with String message
     */
    @DeleteMapping("/{serviceProviderId}/delete")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteServiceProvider(
            @PathVariable Long branchId,
            Authentication authentication,
            @PathVariable Long serviceProviderId) {
        try {
            serviceProviderService.deleteServiceProvider(branchId, serviceProviderId, authentication.getName());
            return ResponseEntity.ok(new ApiResponse<>(true, "Service Provider deleted successfully", null));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
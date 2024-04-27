package EasyAppointment.appointmentscheduler.controllers;

import EasyAppointment.appointmentscheduler.DTO.BranchDTO;
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

@RestController
@RequestMapping("/api/business/{businessId}/{branchId}/service-provider")
@RequiredArgsConstructor
public class ServiceProviderController {
    private final ServiceProviderService serviceProviderService;


    @GetMapping("/get-all")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<ServiceProviderDTO>>> getBranchesByAuthenticatedBusinessOwner(
            @PathVariable Long businessId,
            @PathVariable Long branchId) {

            return ResponseEntity.ok(serviceProviderService.getServiceProviderListByBranch(branchId,businessId));
    }

    @GetMapping("/{serviceProviderId}/get")
    public ResponseEntity<ApiResponse<ServiceProviderDTO>> getServiceProviderById(
            @PathVariable Long branchId,
            @PathVariable Long businessId,
            @PathVariable Long serviceProviderId) {
        System.out.println("get service provider by id request: " + branchId + " businessId: " + businessId + " serviceProviderId: " + serviceProviderId);

            return ResponseEntity.ok(serviceProviderService.getServiceProvidersById(branchId, serviceProviderId,businessId));
    }

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

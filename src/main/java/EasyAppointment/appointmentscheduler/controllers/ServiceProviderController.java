package EasyAppointment.appointmentscheduler.controllers;

import EasyAppointment.appointmentscheduler.DTO.BranchDTO;
import EasyAppointment.appointmentscheduler.DTO.ServiceProviderDTO;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiRequest;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiResponse;
import EasyAppointment.appointmentscheduler.services.ServiceProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/{businessId}/{branchId}/service-provider")
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
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<ServiceProviderDTO>> getServiceProviderById(
            @PathVariable Long branchId,
            @PathVariable Long businessId,
            @PathVariable Long serviceProviderId) {

            return ResponseEntity.ok(serviceProviderService.getServiceProvidersById(branchId, serviceProviderId,businessId));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<ServiceProviderDTO>> createServiceProvider(
            @RequestBody ApiRequest<ServiceProviderDTO> request,
            Authentication authentication,
            @PathVariable Long branchId) {
        try {
            return ResponseEntity.ok(serviceProviderService.addServiceProvider(branchId, request, authentication.getName()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<ServiceProviderDTO>builder()
                            .success(false)
                            .message(e.getMessage())
                            .data(null)
                            .build());
        }
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

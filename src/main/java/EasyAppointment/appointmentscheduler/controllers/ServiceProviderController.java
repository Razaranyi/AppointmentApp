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
@RequestMapping("/api/serviceProvider")
@RequiredArgsConstructor
public class ServiceProviderController {
    private final ServiceProviderService serviceProviderService;


    @GetMapping("/get-all")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<List<ServiceProviderDTO>>> getBranchesByAuthenticatedBusinessOwner(
            @RequestBody ApiRequest<BranchDTO> request) {
        try {
            Long id = request.getData().getBranchId();
            return ResponseEntity.ok(serviceProviderService.getServiceProviderListByBranch(id));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<List<EasyAppointment.appointmentscheduler.DTO.ServiceProviderDTO>>builder()
                            .success(false)
                            .message(e.getMessage())
                            .data(null)
                            .build());
        }
    }

    @GetMapping("/get/branch/{branchId}/ServiceProvider/{serviceProviderId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<ServiceProviderDTO>> getServiceProviderById(
            @PathVariable Long branchId,
            @PathVariable Long serviceProviderId) {
        try {
            return ResponseEntity.ok(serviceProviderService.getServiceProvidersById(branchId, serviceProviderId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<ServiceProviderDTO>builder()
                            .success(false)
                            .message(e.getMessage())
                            .data(null)
                            .build());
        }
    }

    @PostMapping("/{branchId}/create")
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

    @DeleteMapping("/{branchId}/delete/{serviceProviderId}")
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

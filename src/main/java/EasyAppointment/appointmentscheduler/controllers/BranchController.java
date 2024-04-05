package EasyAppointment.appointmentscheduler.controllers;

import EasyAppointment.appointmentscheduler.DTO.BranchDTO;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiRequest;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiResponse;
import EasyAppointment.appointmentscheduler.services.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/business/{businessId}/branch")
@RequiredArgsConstructor
public class BranchController {
    private final BranchService branchService;

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<BranchDTO>>> getBranchesByAuthenticatedBusinessOwner(@PathVariable String businessId) {
        try {
            return ResponseEntity.ok(branchService.getBranchesByAuthenticatedBusinessOwner());
        }catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<List<BranchDTO>>builder()
                            .success(false)
                            .message(e.getMessage())
                            .data(null)
                            .build());
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BranchDTO>> createBranch(
            @RequestBody ApiRequest<BranchDTO> request,
            Authentication authentication,
            @PathVariable String businessId) {
        try {
            String userEmail = authentication.getName();
            return ResponseEntity.ok(branchService.addBranch(request, userEmail)); //change to businessId instead of userEmail and authenticate with helper
        }catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<BranchDTO>builder()
                            .success(false)
                            .message(e.getMessage())
                            .data(null)
                            .build());
        }
    }


}

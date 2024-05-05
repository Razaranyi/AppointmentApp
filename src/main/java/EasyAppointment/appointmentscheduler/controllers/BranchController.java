package EasyAppointment.appointmentscheduler.controllers;

import EasyAppointment.appointmentscheduler.DTO.BranchDTO;
import EasyAppointment.appointmentscheduler.auth.AuthHelper;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiRequest;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiResponse;
import EasyAppointment.appointmentscheduler.services.BranchService;
import EasyAppointment.appointmentscheduler.util.ControllerUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This is the controller for the Branch entity.
 * It handles HTTP requests and responses related to Branch operations.
 */
@RestController
@RequestMapping("/api/business/{businessId}/branch")
@RequiredArgsConstructor
public class BranchController {
    private final BranchService branchService;

    /**
     * This method handles the GET request to retrieve all branches for a specific business ID.
     * @param businessId The ID of the business.
     * @return ResponseEntity containing ApiResponse with List of BranchDTO
     */
    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<BranchDTO>>> getBranchesForBusinessId(@PathVariable long businessId) {
        try {
            return ResponseEntity.ok(branchService.getBranchesForBusinessId(businessId));
        }catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<List<BranchDTO>>builder()
                            .success(false)
                            .message(e.getMessage())
                            .data(null)
                            .build());
        }
    }

    /**
     * This method handles the POST request to create a new branch.
     * @param request The request body containing the branch details to be created.
     * @param businessId The ID of the business.
     * @param result BindingResult object to hold validation results.
     * @return ResponseEntity containing ApiResponse with BranchDTO
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BranchDTO>> createBranch(@Valid
            @RequestBody ApiRequest<BranchDTO> request,
            @PathVariable String businessId,
            BindingResult result) {
        if (result.hasErrors()) {
            String errorMessages = ControllerUtils.getErrorMessages(result);
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, errorMessages, null));
        }
        String userEmail = AuthHelper.getCaller();
        return ResponseEntity.ok(branchService.addBranch(request, userEmail));

    }

    /**
     * This method handles the GET request to retrieve the branch ID.
     * @param businessId The ID of the business.
     * @param branchName The name of the branch.
     * @return ResponseEntity containing ApiResponse with BranchDTO
     */
    @GetMapping("/{branchName}/get-id")
    public ResponseEntity<ApiResponse<BranchDTO>> getBranchId(@PathVariable Long businessId, @PathVariable String branchName) {

        return ResponseEntity.ok(branchService.getBranchId(businessId, branchName));
    }
}
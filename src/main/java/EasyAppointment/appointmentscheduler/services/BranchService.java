package EasyAppointment.appointmentscheduler.services;

import EasyAppointment.appointmentscheduler.DTO.BranchDTO;
import EasyAppointment.appointmentscheduler.models.Branch;
import EasyAppointment.appointmentscheduler.models.Business;
import EasyAppointment.appointmentscheduler.models.User;
import EasyAppointment.appointmentscheduler.repositories.BranchRepository;
import EasyAppointment.appointmentscheduler.repositories.BusinessRepository;
import EasyAppointment.appointmentscheduler.repositories.UserRepository;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiRequest;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class BranchService {
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    public ApiResponse<BranchDTO> addBranch(ApiRequest<BranchDTO> request, String userEmail) throws RuntimeException {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));
        Business business = businessRepository.findByUsersContains(user)
                .orElseThrow(() -> new RuntimeException("Business not found for user: " + userEmail));
        Branch newBranch = Branch.builder()
                .name(request.getData().getName())
                .address(request.getData().getAddress())
                .business(business)
                .serviceProviders(request.getData().getServiceProviders())
                .build();
        business.getBranches().add(newBranch);
        businessRepository.save(business); // save business with new branch

        BranchDTO branchDTO = new BranchDTO(newBranch);

        return new ApiResponse<>(true, "branch created successfully", branchDTO);

    }


}

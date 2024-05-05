package EasyAppointment.appointmentscheduler.services;

import EasyAppointment.appointmentscheduler.DTO.BranchDTO;
import EasyAppointment.appointmentscheduler.exception.UserAlreadyOwnException;
import EasyAppointment.appointmentscheduler.models.Branch;
import EasyAppointment.appointmentscheduler.models.Business;
import EasyAppointment.appointmentscheduler.models.ServiceProvider;
import EasyAppointment.appointmentscheduler.models.User;
import EasyAppointment.appointmentscheduler.repositories.BranchRepository;
import EasyAppointment.appointmentscheduler.repositories.BusinessRepository;
import EasyAppointment.appointmentscheduler.repositories.ServiceProviderRepository;
import EasyAppointment.appointmentscheduler.repositories.UserRepository;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiRequest;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class BranchService {
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final BranchRepository branchRepository;
    private final ServiceProviderRepository serviceProviderRepository;

    @Transactional(readOnly = false)
    public ApiResponse<BranchDTO> addBranch(ApiRequest<BranchDTO> request, String userEmail) throws RuntimeException {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));

        Business business = businessRepository.findByUsersContains(user)
                .orElseThrow(() -> new RuntimeException("Business not found for user: " + userEmail));

        Set<ServiceProvider> serviceProviders = new HashSet<>();

        if (request.getData().getServiceProvidersIds() != null) {
            serviceProviders = new HashSet<>(serviceProviderRepository.findAllById(request.getData().getServiceProvidersIds()));
        }

        if (branchRepository.existsByNameAndBusiness(request.getData().getName(), business)) {
            throw new UserAlreadyOwnException("Branch with name " + request.getData().getName() + " already exists for business");
        }

        Branch newBranch = Branch.builder()
                .name(request.getData().getName())
                .address(request.getData().getAddress())
                .business(business)
                .openingHours(request.getData().getOpeningHours())
                .closingHours(request.getData().getClosingHours())
                .branchImage(request.getData().getBranchImage())
                .serviceProviders(serviceProviders)
                .build();
        business.getBranches().add(newBranch);
        businessRepository.save(business); // save business with new branch, using cascade

        BranchDTO branchDTO = new BranchDTO(newBranch);

        return new ApiResponse<>(true, "branch created successfully", branchDTO);
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<BranchDTO>> getBranchesForBusinessId(long businessId) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found with id: " + businessId));
        List<Branch> branches = branchRepository.findByBusiness(business);

        List<BranchDTO> branchDTOs = branches.stream()
                .map(BranchDTO::new)
                .toList();
        return new ApiResponse<>(true, "Branches fetched successfully", branchDTOs);
    }

    @Transactional(readOnly = true)
    public ApiResponse<BranchDTO> getBranchId(Long businessId, String branchName) {
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found with id: " + businessId));
        Branch branch = (Branch) branchRepository.findByNameAndBusiness(branchName, business)
                .orElseThrow(() -> new RuntimeException("Branch not found with name: " + branchName));
        BranchDTO branchDTO = new BranchDTO(branch);
        System.out.println("branchDTO: "+ branchDTO.toString());
        return new ApiResponse<>(true, "Branch fetched successfully", branchDTO);
    }
}

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
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class provides services related to branches.
 * It uses Spring's @Service annotation to indicate that it's a service class.
 * It uses Lombok's @RequiredArgsConstructor to automatically generate a constructor with required fields.
 */
@Service
@RequiredArgsConstructor
public class BranchService {
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final BranchRepository branchRepository;
    private final ServiceProviderRepository serviceProviderRepository;


    /**
     * This method adds a new branch.
     * It uses Spring's @Transactional annotation to ensure the operation is performed within a transaction.
     * @param request The request containing the branch data.
     * @param userEmail The email of the user who is adding the branch.
     * @return An ApiResponse object containing the result of the operation.
     * @throws RuntimeException if the user or business is not found, or if the branch already exists.
     */
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

    /**
     * This method retrieves all branches for a specific business.
     * It uses Spring's @Transactional annotation to ensure the operation is performed within a transaction.
     * @param businessId The ID of the business.
     * @return An ApiResponse object containing the result of the operation.
     */
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


    /**
     * This method retrieves the ID of a specific branch for a specific business.
     * It uses Spring's @Transactional annotation to ensure the operation is performed within a transaction.
     * @param businessId The ID of the business.
     * @param branchName The name of the branch.
     * @return An ApiResponse object containing the result of the operation.
     */
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

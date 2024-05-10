package EasyAppointment.appointmentscheduler.services;

import EasyAppointment.appointmentscheduler.DTO.ServiceProviderDTO;
import EasyAppointment.appointmentscheduler.exception.UserAlreadyOwnException;
import EasyAppointment.appointmentscheduler.models.*;
import EasyAppointment.appointmentscheduler.repositories.*;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiRequest;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

/**
 * This class provides services related to service providers.
 * It uses Spring's @Service annotation to indicate that it's a service class.
 * It uses Lombok's @RequiredArgsConstructor to automatically generate a constructor with required fields.
 */
@Service
@RequiredArgsConstructor
public class ServiceProviderService {
    private final BranchRepository branchRepository;
    private final ServiceProviderRepository serviceProviderRepository;
    private final BusinessRepository businessRepository;
    private final AppointmentService appointmentService;

    /**
     * This method retrieves a list of service providers by branch.
     * It uses Spring's @Transactional annotation to ensure the operation is performed within a transaction.
     * @param branchId The ID of the branch.
     * @param businessId The ID of the business.
     * @return An ApiResponse object containing the result of the operation.
     */
    @Transactional(readOnly = true)
    public ApiResponse<List<ServiceProviderDTO>> getServiceProviderListByBranch(Long branchId,Long businessId) {
        System.out.println("get service provider list by branch request: " + branchId + " businessId: " + businessId);
        Optional<Branch> branchOptional = branchRepository.findById(branchId);
        if (branchOptional.isEmpty()) {
            throw new NoSuchElementException("Branch not found");
        }
        if (isBranchBelongsToBusiness(branchOptional.get(), businessRepository.getReferenceById(businessId))) {
            throw new IllegalArgumentException("Wrong branch or business provided");
        }

        Set<ServiceProvider> serviceProviders = branchOptional.get().getServiceProviders();
        List<ServiceProviderDTO> serviceProviderDTOs = serviceProviders.stream()
                .map(ServiceProviderDTO::new)
                .collect(Collectors.toList());

        return new ApiResponse<>(true, "Service Providers fetched successfully", serviceProviderDTOs);
    }

    /**
     * This method retrieves a service provider by its ID.
     * It uses Spring's @Transactional annotation to ensure the operation is performed within a transaction.
     * @param branchId The ID of the branch.
     * @param serviceProviderId The ID of the service provider.
     * @param businessId The ID of the business.
     * @return An ApiResponse object containing the result of the operation.
     */
    @Transactional
    public ApiResponse<ServiceProviderDTO> getServiceProvidersById(Long branchId, Long serviceProviderId, Long businessId) {
        Optional<Branch> branchOptional = branchRepository.findById(branchId);
        if (branchOptional.isEmpty()) {
            throw new NoSuchElementException("Branch not found");
        }

        Branch branch = branchOptional.get();
        ServiceProvider serviceProvider = serviceProviderRepository.findById(serviceProviderId)
                .orElseThrow(() -> new NoSuchElementException("Service Provider not found"));

        // Check if the service provider belongs to the given branch
        if (!isServiceProviderBelongsToBranch(branch, serviceProvider) || isBranchBelongsToBusiness(branch, businessRepository.getReferenceById(businessId))) {
            throw new IllegalArgumentException("Wrong branch or business provided");
        }

        ServiceProviderDTO serviceProviderDTO = new ServiceProviderDTO(serviceProvider);
        return new ApiResponse<>(true, "Service Provider fetched successfully", serviceProviderDTO);
    }
    /**
     * This method adds a service provider.
     * It uses Spring's @Transactional annotation to ensure the operation is performed within a transaction.
     * @param branchId The ID of the branch.
     * @param request The request containing the service provider data.
     * @param userEmail The email of the user who is adding the service provider.
     * @return An ApiResponse object containing the result of the operation.
     */
    @Transactional
    public ApiResponse<ServiceProviderDTO> addServiceProvider(Long branchId, ApiRequest<ServiceProviderDTO> request,String userEmail) {
        ServiceProvider serviceProvider;
        System.out.println("Request: " + request.getData().toString());

        if (branchRepository.findById(branchId).isEmpty()){
            throw new NoSuchElementException("Branch not found");
        }

        if (isUserAuthorized(branchId, userEmail)){
            throw new BadCredentialsException("User not authorized");
        }

        if (serviceProviderRepository.existsByNameAndBranchId(request.getData().getName(), branchId)){
            throw new UserAlreadyOwnException("Service Provider already exists in the branch");
        }

        boolean[] workingDays = request.getData().getWorkingDays();
        boolean isWorking = false;
        for (boolean workingDay : workingDays) {
            if (workingDay) {
                isWorking = true;
                break;
            }
        }
        if (!isWorking){
            throw new IllegalArgumentException("Service Provider must have at least one working day");
        }
        System.out.println("Request: " + request.getData().toString());

        try {
            ServiceProvider.ServiceProviderBuilder serviceProviderBuilder = ServiceProvider.builder()
                    .name(request.getData().getName())
                    .workingDays(request.getData().getWorkingDays())
                    .branch(branchRepository.findById(branchId).get())
                    .sessionDuration(request.getData().getSessionDuration());


            if (request.getData().getBreakHour() != null){
                serviceProviderBuilder.breakHour(Arrays.toString(request.getData().getBreakHour()));
            }

            if (request.getData().getServiceProviderImage() != null){
                serviceProviderBuilder.serviceProviderImage(request.getData().getServiceProviderImage());
            }

            serviceProvider = serviceProviderBuilder.build();
            serviceProvider = serviceProviderRepository.save(serviceProvider); // Save the serviceProvider object to the database
            appointmentService.generateAndSaveServiceProviderSchedule(serviceProvider, branchRepository.findById(branchId).get());
            branchRepository.findById(branchId).get().getServiceProviders().add(serviceProvider);
        } catch (Exception e) {
            throw new RuntimeException("Error in adding service provider: " + e.getMessage());
        }

        return new ApiResponse<>(true, "Service Provider added successfully", new ServiceProviderDTO(serviceProvider));
    }

    /**
     * This method deletes a service provider.
     * It uses Spring's @Transactional annotation to ensure the operation is performed within a transaction.
     * @param branchId The ID of the branch.
     * @param serviceProviderId The ID of the service provider.
     * @param userEmail The email of the user who is deleting the service provider.
     * @return An ApiResponse object containing the result of the operation.
     */
    @Transactional
    public ApiResponse<ServiceProvider> deleteServiceProvider(
            Long branchId, Long serviceProviderId, String userEmail) {
        ServiceProvider serviceProvider;
        try{
            if (branchRepository.findById(branchId).isEmpty()){
                return new ApiResponse<>(false, "Branch not found", null);
            }
            if (isUserAuthorized(branchId, userEmail)){
                return new ApiResponse<>(false, "User not authorized", null);
            }
            serviceProvider = serviceProviderRepository.findById(serviceProviderId).orElse(null);

            branchRepository.findById(branchId).get().getServiceProviders().remove(serviceProvider);
            branchRepository.save(branchRepository.findById(branchId).get());
        }catch (Exception e){
            return new ApiResponse<>(false, "Error in deleting service provider: " + e.getMessage(), null);
        }
        return new ApiResponse<>(true, "Service Provider deleted successfully", serviceProvider);
    }

    /**
     * This method checks if a user is authorized.
     * @param branchId The ID of the branch.
     * @param userEmail The email of the user.
     * @return A boolean indicating whether the user is authorized.
     */
    private boolean isUserAuthorized(Long branchId, String userEmail){
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()){
            return true;
        }
        Set<User> users = optionalBranch.get().getBusiness().getUsers();
        return users.stream().noneMatch(user -> user.getEmail().equals(userEmail));
    }

    /**
     * This method checks if a service provider belongs to a branch.
     * @param branch The Branch object.
     * @param serviceProvider The ServiceProvider object.
     * @return A boolean indicating whether the service provider belongs to the branch.
     */
    private boolean isServiceProviderBelongsToBranch(Branch branch, ServiceProvider serviceProvider){
        return branch.getServiceProviders().contains(serviceProvider);
    }

    /**
     * This method checks if a branch belongs to a business.
     * @param branch The Branch object.
     * @param business The Business object.
     * @return A boolean indicating whether the branch belongs to the business.
     */
    private boolean isBranchBelongsToBusiness(Branch branch, Business business){
        return !branch.getBusiness().equals(business);
    }
}
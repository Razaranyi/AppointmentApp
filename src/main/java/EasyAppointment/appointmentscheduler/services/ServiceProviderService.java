package EasyAppointment.appointmentscheduler.services;

import EasyAppointment.appointmentscheduler.DTO.ServiceProviderDTO;
import EasyAppointment.appointmentscheduler.models.*;
import EasyAppointment.appointmentscheduler.repositories.AppointmentRepository;
import EasyAppointment.appointmentscheduler.repositories.BranchRepository;
import EasyAppointment.appointmentscheduler.repositories.BusinessRepository;
import EasyAppointment.appointmentscheduler.repositories.ServiceProviderRepository;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiRequest;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceProviderService {
    private final BranchRepository branchRepository;
    private final ServiceProviderRepository serviceProviderRepository;
    private final AppointmentRepository appointmentRepository;
    private final BusinessRepository businessRepository;
    private final AppointmentService appointmentService;

    public ApiResponse<List<ServiceProviderDTO>> getServiceProviderListByBranch(Long branchId,Long businessId) {
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
        try {
            serviceProvider = ServiceProvider.builder()
                    .name(request.getData().getName())
                    .workingDays(request.getData().getWorkingDays())
                    .breakHour(Arrays.toString(request.getData().getBreakHour()))
                    .branch(branchRepository.findById(branchId).get())
                    .build();

            //add service provider to the branch
            serviceProvider = serviceProviderRepository.save(serviceProvider);

            appointmentService.generateAndSaveServiceProviderSchedule(serviceProvider, serviceProvider.getBranch());
        }catch (Exception e){
            throw new RuntimeException("Error in adding service provider: " + e.getMessage());
        }

        return new ApiResponse<>(true, "Service Provider added successfully", new ServiceProviderDTO(serviceProvider));
    }

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

    private boolean isUserAuthorized(Long branchId, String userEmail){
        Optional<Branch> optionalBranch = branchRepository.findById(branchId);
        if (optionalBranch.isEmpty()){
            return true;
        }
        Set<User> users = optionalBranch.get().getBusiness().getUsers();
        return users.stream().noneMatch(user -> user.getEmail().equals(userEmail));
    }
    private boolean isServiceProviderBelongsToBranch(Branch branch, ServiceProvider serviceProvider){
        return branch.getServiceProviders().contains(serviceProvider);
    }
    private boolean isBranchBelongsToBusiness(Branch branch, Business business){
        return !branch.getBusiness().equals(business);
    }
}
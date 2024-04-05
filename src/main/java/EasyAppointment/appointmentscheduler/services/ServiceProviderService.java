package EasyAppointment.appointmentscheduler.services;

import EasyAppointment.appointmentscheduler.DTO.ServiceProviderDTO;
import EasyAppointment.appointmentscheduler.models.Branch;
import EasyAppointment.appointmentscheduler.models.ServiceProvider;
import EasyAppointment.appointmentscheduler.models.User;
import EasyAppointment.appointmentscheduler.repositories.BranchRepository;
import EasyAppointment.appointmentscheduler.repositories.ServiceProviderRepository;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiRequest;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
@RequiredArgsConstructor
public class ServiceProviderService {
    private final BranchRepository branchRepository;
    private final ServiceProviderRepository serviceProviderRepository;
    public ApiResponse<List<ServiceProviderDTO>> getServiceProviderListByBranch(Long branchId) {
        List<ServiceProviderDTO> serviceProviderDTOs;
        try{
            if (branchRepository.findById(branchId).isEmpty()){ //check if branch exists
                throw new NoSuchElementException("Branch not found");
            }

            //get service providers from branch and return it as a list of DTOs
            Set<ServiceProvider> serviceProviders = branchRepository.findById(branchId).get().getServiceProviders();
            serviceProviderDTOs = serviceProviders.stream()
                    .map(ServiceProviderDTO::new)
                    .toList();
        }catch (Exception e){
            throw new RuntimeException("Error in fetching service providers: " + e.getMessage());
        }
        return new ApiResponse<>(true, "Service Providers fetched successfully", serviceProviderDTOs);
    }

    public ApiResponse<ServiceProviderDTO> getServiceProvidersById(Long branchId, Long serviceProviderId) {
        ServiceProviderDTO serviceProviderDTO;
        try{
            if (branchRepository.findById(branchId).isEmpty()){
                return new ApiResponse<>(false, "Branch not found", null);
            }

            ServiceProvider serviceProvider = serviceProviderRepository.findById(serviceProviderId).orElseThrow(()
                    -> new NoSuchElementException("Service Provider not found"));
            serviceProviderDTO = new ServiceProviderDTO(serviceProvider);
        }catch (Exception e){
            throw  new RuntimeException("Error in fetching service provider: " + e.getMessage());
        }
        return new ApiResponse<>(true, "Service Provider fetched successfully", serviceProviderDTO);
    }

    public ApiResponse<ServiceProviderDTO> addServiceProvider(Long branchId, ApiRequest<ServiceProviderDTO> request,String userEmail) {
        ServiceProvider serviceProvider;

        try{
            if (branchRepository.findById(branchId).isEmpty()){
                return new ApiResponse<>(false, "Branch not found", null);
            }

            if (!isUserAuthorized(branchId,userEmail)){
                return new ApiResponse<>(false, "User not authorized", null);
            }

            serviceProvider = ServiceProvider.builder()
                    .name(request.getData().getName())
                    .workingDays(request.getData().getWorkingDays())
                    .breakTime(Arrays.toString(request.getData().getBreakTime()))
                    .branch(branchRepository.findById(branchId).get())
                    .build();

            //add service provider to the branch
            branchRepository.findById(branchId).get().getServiceProviders().add(serviceProvider);
            branchRepository.save(branchRepository.findById(branchId).get());

        }catch (Exception e){
            return new ApiResponse<>(false, e.getMessage(), null);
        }
        return new ApiResponse<>(true, "Service Provider added successfully", new ServiceProviderDTO(serviceProvider));
    }

    public ApiResponse<ServiceProvider> deleteServiceProvider(
            Long branchId, Long serviceProviderId, String userEmail) {
        ServiceProvider serviceProvider;
        try{
            if (branchRepository.findById(branchId).isEmpty()){
                return new ApiResponse<>(false, "Branch not found", null);
            }
            if (!isUserAuthorized(branchId,userEmail)){
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
            return false;
        }
        Set<User> users = optionalBranch.get().getBusiness().getUsers();
        return users.stream().anyMatch(user -> user.getEmail().equals(userEmail));
    }

}

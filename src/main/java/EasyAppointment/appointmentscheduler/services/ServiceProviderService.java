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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ServiceProviderService {
    BranchRepository branchRepository;
    ServiceProviderRepository serviceProviderRepository;
    public ApiResponse<List<ServiceProviderDTO>> getServiceProviderListByBranch(Long branchId) {
        List<ServiceProviderDTO> serviceProviderDTOs;
        try{
            if (branchRepository.findById(branchId).isEmpty()){ //check if branch exists
                return new ApiResponse<>(false, "Branch not found", null);
            }

            //get service providers from branch and return it as a list of DTOs
            Set<ServiceProvider> serviceProviders = branchRepository.findById(branchId).get().getServiceProviders();
            serviceProviderDTOs = serviceProviders.stream()
                    .map(ServiceProviderDTO::new)
                    .toList();
        }catch (Exception e){
            return new ApiResponse<>(false, "Error in processing service providers", null);
        }
        return new ApiResponse<>(true, "Service Providers fetched successfully", serviceProviderDTOs);
    }

    public ApiResponse<ServiceProviderDTO> getServiceProvidersById(Long branchId, Long serviceProviderId) {
        ServiceProviderDTO serviceProviderDTO;
        try{
            if (branchRepository.findById(branchId).isEmpty()){
                return new ApiResponse<>(false, "Branch not found", null);
            }
            ServiceProvider serviceProvider = branchRepository.findById(branchId).get().getServiceProviders().stream()
                    .filter(sp -> sp.getId().equals(serviceProviderId))
                    .findFirst()
                    .orElse(null);
            if (serviceProvider == null){
                return new ApiResponse<>(false, "Service Provider not found", null);
            }
            serviceProviderDTO = new ServiceProviderDTO(serviceProvider);
        }catch (Exception e){
            return new ApiResponse<>(false, "Error in processing service provider", null);
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
                    .build();

            //add service provider to the branch
            branchRepository.findById(branchId).get().getServiceProviders().add(serviceProvider);
            branchRepository.save(branchRepository.findById(branchId).get());

        }catch (Exception e){
            return new ApiResponse<>(false, "Error in adding service provider", null);
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
            return new ApiResponse<>(false, "Error in deleting service provider", null);
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

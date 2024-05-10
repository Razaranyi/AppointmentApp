package EasyAppointment.appointmentscheduler.services;

import EasyAppointment.appointmentscheduler.DTO.BusinessDTO;
import EasyAppointment.appointmentscheduler.auth.AuthHelper;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiRequest;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiResponse;
import EasyAppointment.appointmentscheduler.exception.UserAlreadyOwnException;
import EasyAppointment.appointmentscheduler.models.Business;
import EasyAppointment.appointmentscheduler.models.Category;
import EasyAppointment.appointmentscheduler.models.User;
import EasyAppointment.appointmentscheduler.repositories.BusinessRepository;
import EasyAppointment.appointmentscheduler.repositories.CategoryRepository;
import EasyAppointment.appointmentscheduler.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import EasyAppointment.appointmentscheduler.models.Role;

import java.util.*;
import java.util.stream.Collectors;


/**
 * This class provides services related to businesses.
 * It uses Spring's @Service annotation to indicate that it's a service class.
 * It uses Lombok's @RequiredArgsConstructor to automatically generate a constructor with required fields.
 */
@Service
@RequiredArgsConstructor
public class BusinessService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final BusinessRepository businessRepository;


    /**
     * This method adds a new business.
     * It uses Spring's @Transactional annotation to ensure the operation is performed within a transaction.
     * @param request The request containing the business data.
     * @param userEmail The email of the user who is adding the business.
     * @return An ApiResponse object containing the result of the operation.
     * @throws UserAlreadyOwnException if the user already owns a business.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Transactional
    public ApiResponse<BusinessDTO> addBusiness(ApiRequest<BusinessDTO> request, String userEmail)
            throws UserAlreadyOwnException, UsernameNotFoundException {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));

//        if (userRepository.existsByEmailAndBusinessIsNotNull(userEmail)) {
//            throw new UserAlreadyOwnException("User with email " + userEmail + " already owns a business");
//        }
        if (!(request.getData().getBusinessCategories().size()>1)){
            throw new IllegalArgumentException("Business must have categories");
        }

        if (businessRepository.existsByName(request.getData().getName())) {
            throw new IllegalArgumentException("Business with name " + request.getData().getName() + " already exists");
        }

        System.out.println("Business categories: " + request.getData().getBusinessCategories());

        // Convert category names to category entities
        Set<String> categoryNames = request.getData().getBusinessCategories();
        Set<Category> categories = categoryNames.stream()
                .map(name -> categoryRepository.findByName(name)
                        .orElseGet(() -> categoryRepository.save(new Category(name))))
                .collect(Collectors.toSet());



        Business newBusiness = Business.builder()
                .name(request.getData().getName())
                .businessCategories(categories)
                .users(new HashSet<>(Collections.singletonList(user))) // Associate the user with the new business
                .logoImage(request.getData().getLogoImage())
                .build();

        Business savedBusiness = businessRepository.save(newBusiness);
        userService.updateUserRole(userEmail, Role.ADMIN);
        user.setBusiness(newBusiness);
        userRepository.save(user);

        BusinessDTO businessDTO = new BusinessDTO(savedBusiness.getId(),savedBusiness.getName(), savedBusiness.getBusinessCategories());
        return new ApiResponse<>(true, "Business created successfully", businessDTO);
    }


    /**
     * This method retrieves the business associated with a specific email.
     * It uses Spring's @Transactional annotation to ensure the operation is performed within a transaction.
     * @param email The email of the user.
     * @return An ApiResponse object containing the result of the operation.
     */
    @Transactional(readOnly = true)
    public ApiResponse<BusinessDTO> getBusinessByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        Business business = user.getBusiness();
        if (business == null) {
            throw new NoSuchElementException("Business not found");
        }
        return new ApiResponse<>(true, "Business found", new BusinessDTO(business));
    }

    /**
     * This method retrieves a business by its ID.
     * It uses Spring's @Transactional annotation to ensure the operation is performed within a transaction.
     * @param id The ID of the business.
     * @return The Business object.
     */
    @Transactional(readOnly = true)
    public Business findById(Long id) {
        return businessRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Business with id " + id + " not found"));
    }

    /**
     * This method retrieves the ID of the business associated with the currently authenticated user.
     * It uses Spring's @Transactional annotation to ensure the operation is performed within a transaction.
     * @return An ApiResponse object containing the result of the operation.
     */
   @Transactional(readOnly = true)
    public ApiResponse<BusinessDTO> getBusinessId() {

        String authenticatedUserEmail = AuthHelper.getCaller();

        User user = userRepository.findByEmail(authenticatedUserEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + authenticatedUserEmail));

        Optional<Business> business = businessRepository.findByUsersContains(user);




        return business
                .map(business1 -> new ApiResponse<>(true, "Business found", new BusinessDTO(business1)))
                .orElseGet(() -> new ApiResponse<>(false, "Business not found", null));
    }


    /**
     * This method retrieves a business by its ID.
     * It uses Spring's @Transactional annotation to ensure the operation is performed within a transaction.
     * @param id The ID of the business.
     * @return An ApiResponse object containing the result of the operation.
     */
  @Transactional(readOnly = true)
   public ApiResponse<BusinessDTO> getBusinessById(Long id) {
        Business business = findById(id);
        return new ApiResponse<>(true, "Business found", new BusinessDTO(business));
    }
}

package EasyAppointment.appointmentscheduler.services;

import EasyAppointment.appointmentscheduler.DTO.BusinessDTO;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiRequest;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiResponse;
import EasyAppointment.appointmentscheduler.requestsAndResponses.business.BusinessCreationRequest;
import EasyAppointment.appointmentscheduler.exception.UserAlreadyOwnsBusinessException;
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


@Service
@RequiredArgsConstructor
public class BusinessService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final BusinessRepository businessRepository;


   @Transactional
    public ApiResponse<BusinessDTO> addBusiness(ApiRequest<BusinessDTO> request, String userEmail)
    throws UserAlreadyOwnsBusinessException, UsernameNotFoundException {
         User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));
         if (userRepository.existsByEmailAndBusinessIsNotNull(userEmail)) {
             throw new UserAlreadyOwnsBusinessException("User with email " + userEmail + " already owns a business");
         }
        Business newBusiness = Business.builder()
                .name(request.getData().getName())
                .businessCategories(request.getData().getBusinessCategories())
                .users(new HashSet<>(Collections.singletonList(user))) // Associate the user with the new business
                .build();
                Business savedBusiness = businessRepository.save(newBusiness);
                userService.updateUserRole(userEmail, Role.ADMIN);
                user.setBusiness(newBusiness);
                userRepository.save(user);

                BusinessDTO businessDTO = new BusinessDTO(savedBusiness.getId(),savedBusiness.getName(), savedBusiness.getBusinessCategories());
        return new ApiResponse<>(true, "Business created successfully", businessDTO);
    }

   @Transactional
    public List<Business> getBusinessesByCategory(String categoryName) {
        return categoryRepository.findByName(categoryName)
                .map(Category::getBusinesses)
                .map(Set::stream)
                .map(stream -> stream.collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Transactional
    public Map<Category, Set<Business>> getBusinessesFromRandomCategories(int numberOfCategories) {
        List<Category> categories = categoryRepository.findAll();
        Collections.shuffle(categories);

        Map<Category, Set<Business>> categoryBusinessMap = new LinkedHashMap<>();
        categories.stream()
                .limit(numberOfCategories)
                .forEach(category -> categoryBusinessMap.put(category, category.getBusinesses()));

        return categoryBusinessMap;
    }

    @Transactional(readOnly = true)
    public Optional<Business> getBusinessByEmail(String email) {
        return userRepository.findByEmail(email)
        .map(User::getBusiness);
    }
}
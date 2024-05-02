package EasyAppointment.appointmentscheduler.services;

import EasyAppointment.appointmentscheduler.DTO.BusinessDTO;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiRequest;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiResponse;
import EasyAppointment.appointmentscheduler.exception.UserAlreadyOwnsBusinessException;
import EasyAppointment.appointmentscheduler.models.Business;
import EasyAppointment.appointmentscheduler.models.Category;
import EasyAppointment.appointmentscheduler.models.User;
import EasyAppointment.appointmentscheduler.repositories.BusinessRepository;
import EasyAppointment.appointmentscheduler.repositories.CategoryRepository;
import EasyAppointment.appointmentscheduler.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
//        if (userRepository.existsByEmailAndBusinessIsNotNull(userEmail)) {
//            throw new UserAlreadyOwnsBusinessException("User with email " + userEmail + " already owns a business");
//        }

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

    public Business findById(Long id) {
        return businessRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Business with id " + id + " not found"));
    }

    public void save(Business business) {
        businessRepository.save(business);
    }

    public ApiResponse<BusinessDTO> addLogoToBusiness(Long id, byte[] logoImage) {
        Business business = findById(id);
        business.setLogoImage(logoImage);
        save(business);
        return new ApiResponse<>(true, "Logo added successfully", new BusinessDTO(business));
    }


   @Transactional(readOnly = true)
    public ApiResponse<BusinessDTO> getBusinessId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUserEmail = authentication.getName();

        User user = userRepository.findByEmail(authenticatedUserEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + authenticatedUserEmail));

        Optional<Business> business = businessRepository.findByUsersContains(user);




        return business
                .map(business1 -> new ApiResponse<>(true, "Business found", new BusinessDTO(business1)))
                .orElseGet(() -> new ApiResponse<>(false, "Business not found", null));
    }

  @Transactional(readOnly = true)
   public ApiResponse<BusinessDTO> getBusinessById(Long id) {
        Business business = findById(id);
        return new ApiResponse<>(true, "Business found", new BusinessDTO(business));
    }
}

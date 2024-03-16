package EasyAppointment.appointmentscheduler.services;

import EasyAppointment.appointmentscheduler.models.Business;
import EasyAppointment.appointmentscheduler.models.Category;
import EasyAppointment.appointmentscheduler.models.User;
import EasyAppointment.appointmentscheduler.repositories.CategoryRepository;
import EasyAppointment.appointmentscheduler.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BusinessService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public List<Business> getBusinessesByCategory(String categoryName) {
        return categoryRepository.findByName(categoryName)
                .map(Category::getBusinesses)
                .map(Set::stream)
                .map(stream -> stream.collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

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
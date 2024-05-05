package EasyAppointment.appointmentscheduler.services;

import EasyAppointment.appointmentscheduler.DTO.CategoryDto;
import EasyAppointment.appointmentscheduler.models.Category;
import EasyAppointment.appointmentscheduler.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides services related to categories.
 * It uses Spring's @Service annotation to indicate that it's a service class.
 * It uses Lombok's @RequiredArgsConstructor to automatically generate a constructor with required fields.
 */
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories(){
        return categoryRepository
                .findAll()
                .stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());
    }

    /**
     * This method initializes categories.
     * It uses Spring's @Transactional annotation to ensure the operation is performed within a transaction.
     * @param categories A list of category names.
     */
   @Transactional
    public void initialCategories(List<String> categories){
        categories.forEach(category -> {
            if(!categoryRepository.existsByName(category)){
                categoryRepository.save(new Category(category));
            }
        });
    }

    /**
     * This method retrieves a random order of categories.
     * It uses Spring's @Transactional annotation to ensure the operation is performed within a transaction.
     * @return A list of CategoryDto objects.
     */
    @Transactional(readOnly = true)
    public List<CategoryDto> getRandomOrderCategories() {
        return categoryRepository
                .findRandomSevenCategories()
                .stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());
    }
}



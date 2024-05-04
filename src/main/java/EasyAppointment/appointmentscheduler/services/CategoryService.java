package EasyAppointment.appointmentscheduler.services;

import EasyAppointment.appointmentscheduler.DTO.CategoryDto;
import EasyAppointment.appointmentscheduler.models.Category;
import EasyAppointment.appointmentscheduler.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

   @Transactional
    public void initialCategories(List<String> categories){
        categories.forEach(category -> {
            if(!categoryRepository.existsByName(category)){
                categoryRepository.save(new Category(category));
            }
        });
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getRandomOrderCategories() {
        return categoryRepository
                .findRandomSevenCategories()
                .stream()
                .map(CategoryDto::new)
                .collect(Collectors.toList());
    }
}



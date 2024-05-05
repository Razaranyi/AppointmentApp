package EasyAppointment.appointmentscheduler.controllers;

import EasyAppointment.appointmentscheduler.DTO.CategoryDto;
import EasyAppointment.appointmentscheduler.repositories.CategoryRepository;
import EasyAppointment.appointmentscheduler.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This is the controller for the Category entity.
 * It handles HTTP requests and responses related to Category operations.
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    /**
     * This method handles the GET request to retrieve all categories.
     * @return ResponseEntity containing List of CategoryDto
     */
    @GetMapping("/initial")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    /**
     * This method handles the GET request to retrieve all categories in random order.
     * @return ResponseEntity containing List of CategoryDto
     */

    @GetMapping("/get-all")
    public ResponseEntity<List<CategoryDto>> getRandomOrderCategories() {
        return ResponseEntity.ok(categoryService.getRandomOrderCategories());
    }

}
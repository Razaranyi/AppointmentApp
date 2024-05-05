package EasyAppointment.appointmentscheduler.repositories;

import EasyAppointment.appointmentscheduler.models.Business;
import EasyAppointment.appointmentscheduler.models.Category;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * This interface represents the repository for the Category entity.
 * It extends JpaRepository to provide CRUD operations for the Category entity.
 * It also declares custom query methods to find a category by name, check if a category exists by name,
 * find all categories, and find seven random categories.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * Finds a category by name.
     * @param name The name of the category.
     * @return An optional category that matches the specified name.
     */
    Optional<Category> findByName(String name);

    /**
     * Finds all categories.
     * @return A list of all categories.
     */
    @Nonnull
    List<Category> findAll();

    /**
     * Checks if a category exists by name.
     * @param category The name of the category.
     * @return true if a category exists that matches the specified name, false otherwise.
     */
    boolean existsByName(String category);

    /**
     * Finds seven random categories.
     * The query selects distinct categories that join with business_category and Business.
     * It checks if there exists a Business that matches the business_id in business_category.
     * It orders the results randomly
     * @return A list of seven random categories.
     */
    @Query(value = "SELECT * FROM (SELECT DISTINCT c.* FROM Category c " +
            "JOIN business_category bc ON c.category_id = bc.category_id " +
            "JOIN Business b ON bc.business_id = b.business_id " +
            "WHERE EXISTS (SELECT 1 FROM Business WHERE business_id = bc.business_id)) AS sub " +
            "ORDER BY RANDOM()", nativeQuery = true)
    List<Category> findRandomSevenCategories();
}
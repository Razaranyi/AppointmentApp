package EasyAppointment.appointmentscheduler.repositories;

import EasyAppointment.appointmentscheduler.models.Business;
import EasyAppointment.appointmentscheduler.models.Category;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    @Nonnull
    List<Category> findAll();

    boolean existsByName(String category);


    @Query(value = "SELECT * FROM (SELECT DISTINCT c.* FROM Category c " +
            "JOIN business_category bc ON c.category_id = bc.category_id " +
            "JOIN Business b ON bc.business_id = b.business_id " +
            "WHERE EXISTS (SELECT 1 FROM Business WHERE business_id = bc.business_id)) AS sub " +
            "ORDER BY RANDOM()", nativeQuery = true)
    List<Category> findRandomSevenCategories();
}
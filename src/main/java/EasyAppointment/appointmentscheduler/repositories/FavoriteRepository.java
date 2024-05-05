package EasyAppointment.appointmentscheduler.repositories;

import EasyAppointment.appointmentscheduler.models.Favorite;
import EasyAppointment.appointmentscheduler.models.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This interface represents the repository for the Favorite entity.
 * It extends JpaRepository to provide CRUD operations for the Favorite entity.
 * It also declares custom query methods to find all favorites by user, check if a favorite exists by user email and business ID,
 * and find a favorite by user email and business ID.
 */
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {


    /**
     * Finds favorites by user.
     * @param user The user entity.
     * @return A list of favorites that belong to the specified user.
     */
    List<Favorite> findByUser(User user);

    /**
     * Checks if a favorite exists by user email and business ID.
     * @param user_email The email of the user.
     * @param business_id The ID of the business.
     * @return true if a favorite exists that matches the specified user email and business ID, false otherwise.
     */
    boolean existsByUserEmailAndBusinessId(@Email(message = "Email is not valid") String user_email, Long business_id);

    /**
     * Finds a favorite by user email and business ID.
     * @param userEmail The email of the user.
     * @param id The ID of the business.
     * @return A favorite that matches the specified user email and business ID.
     */
    Favorite findByUserEmailAndBusinessId(String userEmail, long id);
}
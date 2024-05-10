package EasyAppointment.appointmentscheduler.repositories;

import EasyAppointment.appointmentscheduler.models.Business;
import EasyAppointment.appointmentscheduler.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * This interface represents the repository for the Business entity.
 * It extends JpaRepository to provide CRUD operations for the Business entity.
 * It also declares a custom query method to find a business by a user.
 */
public interface BusinessRepository extends JpaRepository<Business, Long> {
    /**
     * Finds a business by a user.
     * The query checks if the user is contained in the users of the business.
     * @param user The user entity.
     * @return An optional business that contains the specified user.
     */
    Optional<Business> findByUsersContains(User user);


   /**
     * Checks if a business with the specified name exists.
     * @param name The name of the business.
     * @return A boolean indicating whether a business with the specified name exists.
     */

    boolean existsByName(String name);
}
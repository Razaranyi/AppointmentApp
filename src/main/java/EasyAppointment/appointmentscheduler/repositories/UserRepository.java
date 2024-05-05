package EasyAppointment.appointmentscheduler.repositories;

import EasyAppointment.appointmentscheduler.models.Role;
import EasyAppointment.appointmentscheduler.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * This interface represents the repository for the User entity.
 * It extends JpaRepository to provide CRUD operations for the User entity.
 * It also declares custom query methods to find a user by email, find a role by email, and check if a user exists by email and business is not null.
 */
public interface UserRepository extends JpaRepository<User, Long>{
    /**
     * Finds a user by email.
     * @param email The email of the user.
     * @return An optional user that matches the specified email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a role by email.
     * The query selects the role from User where the email matches the specified email.
     * @param email The email of the user.
     * @return The role of the user that matches the specified email.
     */
    @Query("SELECT u.role FROM User u WHERE u.email = :email")
    Role findRoleByEmail(@Param("email") String email);

    /**
     * Checks if a user exists by email and business is not null.
     * @param email The email of the user.
     * @return true if a user exists that matches the specified email and business is not null, false otherwise.
     */
    boolean existsByEmailAndBusinessIsNotNull(String email);
}
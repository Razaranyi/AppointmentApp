package EasyAppointment.appointmentscheduler.repositories;

import EasyAppointment.appointmentscheduler.models.Favorite;
import EasyAppointment.appointmentscheduler.models.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByUser(User user);
    List<Favorite> findByUser(User user);


    boolean existsByUserEmailAndBusinessId(@Email(message = "Email is not valid") String user_email, Long business_id);
}

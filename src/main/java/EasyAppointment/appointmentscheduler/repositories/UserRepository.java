package EasyAppointment.appointmentscheduler.repositories;

import EasyAppointment.appointmentscheduler.models.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
    Optional<User> findByFullName(String fullName);


    List<User> findAllByEmail(String email);
}

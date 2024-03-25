package EasyAppointment.appointmentscheduler.repositories;

import EasyAppointment.appointmentscheduler.models.Business;
import EasyAppointment.appointmentscheduler.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, Long> {
    Optional<Business> findByUsersContains(User user);

}

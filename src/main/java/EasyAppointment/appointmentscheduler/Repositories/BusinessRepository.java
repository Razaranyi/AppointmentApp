package EasyAppointment.appointmentscheduler.Repositories;

import EasyAppointment.appointmentscheduler.models.Business;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessRepository extends JpaRepository<Business, Long> {
}

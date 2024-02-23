package EasyAppointment.appointmentscheduler.Repositories;

import EasyAppointment.appointmentscheduler.models.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {
}

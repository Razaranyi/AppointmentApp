package EasyAppointment.appointmentscheduler.repositories;

import EasyAppointment.appointmentscheduler.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByServiceProviderIdAndStartTimeBetween(Long serviceProviderId, LocalDateTime localDateTime, LocalDateTime localDateTime1);
}

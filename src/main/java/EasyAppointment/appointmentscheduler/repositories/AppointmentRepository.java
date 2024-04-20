package EasyAppointment.appointmentscheduler.repositories;

import EasyAppointment.appointmentscheduler.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByServiceProviderIdAndStartTimeBetween(Long serviceProviderId, LocalDateTime localDateTime, LocalDateTime localDateTime1);


    Optional<Appointment> findByStartTimeAndEndTimeAndServiceProviderId(LocalDateTime startTime, LocalDateTime endTime, Long serviceProviderId);
}

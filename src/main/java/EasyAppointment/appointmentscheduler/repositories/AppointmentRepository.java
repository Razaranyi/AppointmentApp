package EasyAppointment.appointmentscheduler.repositories;

import EasyAppointment.appointmentscheduler.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This interface represents the repository for the Appointment entity.
 * It extends JpaRepository to provide CRUD operations for the Appointment entity.
 * It also declares a custom query method to find appointments by service provider ID and start time.
 */
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    /**
     * Finds appointments by service provider ID and start time.
     * The start time is between two specified LocalDateTime objects.
     * @param serviceProviderId The ID of the service provider.
     * @param localDateTime The start of the time range.
     * @param localDateTime1 The end of the time range.
     * @return A list of appointments that match the specified criteria.
     */
    List<Appointment> findByServiceProviderIdAndStartTimeBetween(Long serviceProviderId, LocalDateTime localDateTime, LocalDateTime localDateTime1);

}
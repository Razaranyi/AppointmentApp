package EasyAppointment.appointmentscheduler.Repositories;

import EasyAppointment.appointmentscheduler.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}

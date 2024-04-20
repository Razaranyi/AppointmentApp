package EasyAppointment.appointmentscheduler.repositories;

import EasyAppointment.appointmentscheduler.models.Appointment;
import EasyAppointment.appointmentscheduler.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByUserId(Long userId);

    @Query("SELECT DISTINCT b FROM Booking b JOIN b.appointments a WHERE a.id IN :appointmentIds " +
            "GROUP BY b HAVING COUNT(DISTINCT a.id) = :numberOfAppointments")
    Optional<Booking> findBookingByAllAppointments(@Param("appointmentIds") Set<Long> appointmentIds,
                                                   @Param("numberOfAppointments") long numberOfAppointments);
    @Query("SELECT b FROM Booking b JOIN b.appointments a WHERE a.id = :appointmentId")
    Optional<Booking> findByAppointmentId(Long appointmentId);


}

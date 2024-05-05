package EasyAppointment.appointmentscheduler.repositories;

import EasyAppointment.appointmentscheduler.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * This interface represents the repository for the Booking entity.
 * It extends JpaRepository to provide CRUD operations for the Booking entity.
 * It also declares custom query methods to find bookings by user ID and by all appointments.
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {
    /**
     * Finds bookings by user ID.
     * @param userId The ID of the user.
     * @return A list of bookings that belong to the specified user.
     */
    List<Booking> findByUserId(Long userId);

    /**
     * Finds a booking by all appointments.
     * The query selects distinct bookings that join with appointments where the appointment ID is in the specified set.
     * It groups the results by booking and having the count of distinct appointment IDs equal to the specified number of appointments.
     * @param appointmentIds The set of appointment IDs.
     * @param numberOfAppointments The number of appointments.
     * @return An optional booking that matches the specified criteria.
     */
    @Query("SELECT DISTINCT b FROM Booking b JOIN b.appointments a WHERE a.id IN :appointmentIds " +
            "GROUP BY b HAVING COUNT(DISTINCT a.id) = :numberOfAppointments")
    Optional<Booking> findBookingByAllAppointments(@Param("appointmentIds") Set<Long> appointmentIds,
                                                   @Param("numberOfAppointments") long numberOfAppointments);

}
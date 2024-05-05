package EasyAppointment.appointmentscheduler.DTO;

import EasyAppointment.appointmentscheduler.models.Appointment;
import EasyAppointment.appointmentscheduler.models.Booking;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is a data transfer object (DTO) for Booking.
 * It is used to send data over the network or between processes.
 * It includes all the necessary information about a booking.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO implements DTOInterface{
    private Long bookingId;
    private LocalDateTime bookingTime;
    private String status;
    @Getter
    @Setter
    private Set<Long> appointmentsIds;
    private Long userId;
    private Long serviceProviderId;

    /**
     * This constructor is used to create a BookingDTO from a Booking object.
     * It copies all the necessary information from the Booking object to the BookingDTO.
     * @param booking The Booking object to be converted into a BookingDTO.
     */
    public BookingDTO(Booking booking) {
        this.bookingId = booking.getBookingId();
        this.bookingTime = booking.getBookingTime();
        this.status = booking.getStatus();
        this.appointmentsIds = booking.getAppointments().stream().map(Appointment::getId).collect(Collectors.toSet());
        this.userId = booking.getUser().getId();
        assert booking.getServiceProvider() != null;
        this.serviceProviderId = booking.getServiceProvider().getId();
    }

}
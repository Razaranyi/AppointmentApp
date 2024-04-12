package EasyAppointment.appointmentscheduler.DTO;

import EasyAppointment.appointmentscheduler.models.Appointment;
import EasyAppointment.appointmentscheduler.models.Booking;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;


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

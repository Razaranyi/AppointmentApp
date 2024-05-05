package EasyAppointment.appointmentscheduler.DTO;

import EasyAppointment.appointmentscheduler.models.Appointment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AppointmentDTO implements DTOInterface{
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer duration;
    private Boolean isAvailable;
    private Long bookingId;
    private Long serviceProviderId;
    private String bookingUserName;
    private String bookedBusinessName;

    public AppointmentDTO(Appointment appointment) {
        this.id = appointment.getId();
        this.startTime = appointment.getStartTime();
        this.endTime = appointment.getEndTime();
        this.isAvailable = appointment.isAvailable();
        this.bookingId = (appointment.getBooking() != null) ? appointment.getBooking().getBookingId() : null;
        this.serviceProviderId = appointment.getServiceProvider().getId();

        if (appointment.getBooking() != null) {
            this.bookingUserName = appointment.getBooking().getUser().getFullName();
            this.bookedBusinessName = appointment.getServiceProvider().getBranch().getBusiness().getName();
        }

    }
}

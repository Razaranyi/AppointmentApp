package EasyAppointment.appointmentscheduler.DTO;

import EasyAppointment.appointmentscheduler.models.Appointment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO implements DTOInterface{
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer duration;
    private Boolean isAvailable;
    private Long bookingId;
    private Long serviceProviderId;

    public AppointmentDTO(Appointment appointment) {
        this.id = appointment.getId();
        this.startTime = appointment.getStartTime();
        this.endTime = appointment.getEndTime();
        this.isAvailable = appointment.getIsAvailable();
        this.bookingId = (appointment.getBooking() != null) ? appointment.getBooking().getId() : null;
    }
}

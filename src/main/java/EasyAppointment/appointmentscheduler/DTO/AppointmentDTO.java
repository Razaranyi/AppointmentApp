package EasyAppointment.appointmentscheduler.DTO;

import EasyAppointment.appointmentscheduler.models.Appointment;
import lombok.*;

import java.time.LocalDateTime;

/**
 * This is a data transfer object (DTO) for Appointment.
 * It is used to send data over the network or between processes.
 * It includes all the necessary information about an appointment.
 */
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

    /**
     * This constructor is used to create an AppointmentDTO from an Appointment object.
     * It copies all the necessary information from the Appointment object to the AppointmentDTO.
     * @param appointment The Appointment object to be converted into an AppointmentDTO.
     */
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
package EasyAppointment.appointmentscheduler.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

/**
 * This class represents an appointment in the system.
 * It is a JPA entity class, and its table in the database is named "Appointment".
 * It uses Lombok annotations for automatic generation of getters, setters, constructors, and builder.
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    /**
     * The ID of the appointment. It is the primary key in the "Appointment" table.
     * It is generated automatically by a sequence generator named "appointment_sequence".
     */
    @Id
    @SequenceGenerator(
            name = "appointment_sequence",
            sequenceName = "appointment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "appointment_sequence"
    )
    @Getter
    @Column(name = "appointment_id", updatable = false)
    private Long id;

    /**
     * The start time of the appointment.
     * It is a required field, and it must be in the future.
     */
    @Column(name = "start_time", nullable = false, columnDefinition = "TIMESTAMP")
    @NotNull(message = "Start time is required")
//    @Future(message = "Start time must be in the future")
    @Getter
    @Setter
    private LocalDateTime startTime;

    /**
     * The end time of the appointment.
     */
    @Column(name = "end_time", nullable = false, columnDefinition = "TIMESTAMP")
    @Getter
    @Setter
    private LocalDateTime endTime;

    /**
     * The availability of the appointment.
     * It is a required field.
     */
    @Setter
    @Getter
    @Column(name = "is_available", nullable = false, columnDefinition = "BOOLEAN")
    @NotNull(message = "Availability is required")
    public boolean isAvailable;

    /**
     * The duration of the appointment, in minutes.
     * It is a required field, and it must be positive.
     */
    @Getter
    @Setter
    @Column(name = "duration", nullable = false, columnDefinition = "INTEGER")
    @Positive(message = "Duration must be positive")
    private int duration; // in minutes

    /**
     * The booking associated with the appointment.
     * Many appointments can be associated with one booking.
     */
    @Getter
    @Setter
    @ManyToOne // Many appointments can be associated with one booking
    @JoinColumn(name = "booking_id", foreignKey = @ForeignKey(name = "FK_Booking_Appointment"))
    private Booking booking;

    /**
     * The service provider associated with the appointment.
     * Many appointments can be associated with one service provider.
     */
    @Getter
    @Setter
    @ManyToOne // Many appointments can be associated with one service provider
    @JoinColumn(name = "service_provider_id", foreignKey = @ForeignKey(name = "FK_ServiceProvider_Appointment"))
    private ServiceProvider serviceProvider;

    /**
     * The version field for optimistic locking.
     * It is automatically managed by JPA.
     */
    @Version
    private int version;

    /**
     * Returns the availability of the appointment.
     * @return true if the appointment is available, false otherwise.
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Sets the availability of the appointment.
     * @param available the new availability of the appointment.
     */
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
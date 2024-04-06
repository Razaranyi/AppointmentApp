package EasyAppointment.appointmentscheduler.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Getter
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
    @Column(name = "appointment_id", updatable = false)
    private Long id;

    @Column(name = "start_time", nullable = false, columnDefinition = "TIMESTAMP")
    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    @Getter
    @Setter
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false, columnDefinition = "TIMESTAMP")
    @Getter
    @Setter
    private LocalDateTime endTime;

    @Column(name = "is_available", nullable = false, columnDefinition = "BOOLEAN")
    @NotNull(message = "Availability is required")
    private Boolean isAvailable;

    @Getter
    @Setter
    @Column(name = "duration", nullable = false, columnDefinition = "INTEGER")
    @Positive(message = "Duration must be positive")
    private Integer duration; // in minutes

    @Getter
    @Setter
    @ManyToOne // Many appointments can be associated with one booking
    @JoinColumn(name = "booking_id", foreignKey = @ForeignKey(name = "FK_Booking_Appointment"))
    private Booking booking;

    @Getter
    @Setter
    @ManyToOne // Many appointments can be associated with one service provider
    @JoinColumn(name = "service_provider_id", foreignKey = @ForeignKey(name = "FK_ServiceProvider_Appointment"))
    private ServiceProvider serviceProvider;


}
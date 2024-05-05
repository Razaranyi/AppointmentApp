package EasyAppointment.appointmentscheduler.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * This class represents a booking in the system.
 * It is a JPA entity class, and its table in the database is named "booking".
 * It uses Lombok annotations for automatic generation of getters, setters, constructors, and builder.
 */
@Getter
@Entity
@Builder
@Table(name = "booking")
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    /**
     * The ID of the booking. It is the primary key in the "booking" table.
     * It is generated automatically by a sequence generator named "booking_sequence".
     */
    @Id
    @SequenceGenerator(
            name = "booking_sequence",
            sequenceName = "booking_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "booking_sequence"
    )

    @Column(name = "booking_id", updatable = false)
    @Getter
    private Long bookingId;

    /**
     * The time of the booking.
     * It is a required field.
     */
    @Setter
    @Column(name = "booking_time", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime bookingTime;

    /**
     * The status of the booking.
     * It is a required field.
     */
    @Setter
    @Column(name = "status", nullable = false, columnDefinition = "TEXT")
    private String status;

    /**
     * The appointments associated with the booking.
     * One booking can be associated with many appointments.
     */
    @Setter
    @OneToMany(mappedBy = "booking")
    private Set<Appointment> appointments;

    /**
     * The user associated with the booking.
     * Many bookings can be associated with one user.
     */
    @Setter
    @ManyToOne(cascade = CascadeType.ALL)
//    @MapsId("userId")
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_User_Booking"))
    private User user;

    /**
     * The service provider associated with the booking.
     * Many bookings can be associated with one service provider.
     */
    @Setter
    @ManyToOne
    @Nullable
    @JoinColumn(name = "service_provider_id", foreignKey = @ForeignKey(name = "FK_ServiceProvider_Booking"))
    private ServiceProvider serviceProvider;

    /**
     * The version field for optimistic locking.
     * It is automatically managed by JPA.
     */
    @Version
    private int version;

    /**
     * Adds an appointment to the booking.
     * It also sets the booking of the appointment to this booking.
     * @param appointment The appointment to be added.
     */
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        appointment.setBooking(this);
    }

    /**
     * Removes an appointment from the booking.
     * It also sets the booking of the appointment to null.
     * @param appointment The appointment to be removed.
     */
    public void removeAppointment(Appointment appointment) {
        appointments.remove(appointment);
        appointment.setBooking(null);
    }
}
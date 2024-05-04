package EasyAppointment.appointmentscheduler.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Entity
@Builder
@Table(name = "booking")
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
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

    @Setter
    @Column(name = "booking_time", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime bookingTime;

    @Setter
    @Column(name = "status", nullable = false, columnDefinition = "TEXT")
    private String status;

    @Setter
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Appointment> appointments;

    @Setter
    @ManyToOne(cascade = CascadeType.ALL)
//    @MapsId("userId")
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_User_Booking"))
    private User user;

    @Setter
    @ManyToOne
    @Nullable
    @JoinColumn(name = "service_provider_id", foreignKey = @ForeignKey(name = "FK_ServiceProvider_Booking"))
    private ServiceProvider serviceProvider;

//    public Set<Appointment> getAppointments() {
//        return appointments;
//    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        appointment.setBooking(this);
    }

    public void removeAppointment(Appointment appointment) {
        appointments.remove(appointment);
        appointment.setBooking(null);
    }
}
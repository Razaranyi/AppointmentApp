package EasyAppointment.appointmentscheduler.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Entity
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
    private Long bookingId;

    @Setter
    @Column(name = "booking_time", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime bookingTime;

    @Setter
    @Column(name = "status", nullable = false, columnDefinition = "TEXT")
    private String status;

    @Setter
    @OneToMany(mappedBy = "booking")
    private Set<Appointment> appointments;

    @Setter
    @ManyToOne
//    @MapsId("userId")
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_User_Booking"))
    private User user;

    @Setter
    @OneToOne
    @Nullable
    @JoinColumn(name = "service_provider_id", foreignKey = @ForeignKey(name = "FK_ServiceProvider_Booking"))
    private ServiceProvider serviceProvider;



    public Booking(LocalDateTime bookingTime, String status, Set<Appointment> appointments, User user) {
        this.bookingTime = bookingTime;
        this.status = status;
        this.appointments = appointments;
        this.user = user;
    }


    public Long getId() {
        return bookingId;
    }
}
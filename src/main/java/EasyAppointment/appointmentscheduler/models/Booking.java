package EasyAppointment.appointmentscheduler.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "booking")
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
    private Long bookingNumber;
    @Column(name = "booking_time", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime bookingTime;
    @Column(name = "status", nullable = false, columnDefinition = "TEXT")
    private String status;
    @OneToMany(mappedBy = "booking")
    private Set<Appointment> appointments;

    @ManyToOne
//    @MapsId("userId")
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_User_Booking"))
    private User user;

    public Booking() {
    }

    public Booking(LocalDateTime bookingTime, String status, Set<Appointment> appointments, User user) {
        this.bookingTime = bookingTime;
        this.status = status;
        this.appointments = appointments;
        this.user = user;
    }

    public Long getBookingNumber() {
        return bookingNumber;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
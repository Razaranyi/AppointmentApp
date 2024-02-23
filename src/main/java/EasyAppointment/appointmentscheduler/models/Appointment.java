package EasyAppointment.appointmentscheduler.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Appointment {
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

    @Column (name = "start_time", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime startTime;
    @Column(name = "is_available", nullable = false, columnDefinition = "BOOLEAN")
    private Boolean isAvailable;
    @Column(name = "duration", nullable = false, columnDefinition = "INTEGER")
    private Integer duration; // in minutes

    @ManyToOne // Many appointments can be associated with one booking
    @JoinColumn(name = "booking_id", foreignKey = @ForeignKey(name = "FK_Booking_Appointment"))
    private Booking booking;

    @ManyToOne // Many appointments can be associated with one service provider
    @JoinColumn(name = "service_provider_id", foreignKey = @ForeignKey(name = "FK_ServiceProvider_Appointment"))
    private ServiceProvider serviceProvider;

    public Appointment() {
    }

    public Appointment(LocalDateTime startTime, Boolean isAvailable, Integer duration, Booking booking, ServiceProvider serviceProvider) {
        this.startTime = startTime;
        this.isAvailable = isAvailable;
        this.duration = duration;
        this.booking = booking;
        this.serviceProvider = serviceProvider;
    }


    public Long getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }
}
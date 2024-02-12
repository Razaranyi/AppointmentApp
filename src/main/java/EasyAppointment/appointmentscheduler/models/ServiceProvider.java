package EasyAppointment.appointmentscheduler.models;

import jakarta.persistence.*;

@Entity
public class ServiceProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String workingDays;
    private String breakTime;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    // Constructors, getters, and setters
}

package EasyAppointment.appointmentscheduler.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class ServiceProvider {
    @Id
    @SequenceGenerator(
            name = "serviceProvider_sequence",
            sequenceName = "serviceProvider_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "serviceProvider_sequence"
    )
    @Column(name = "service_provider_id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(name = "working_days", nullable = false, columnDefinition = "TEXT")
    private String workingDays;

    @Column(name = "working_hours", nullable = false, columnDefinition = "TEXT")
    private String breakTime;

    @OneToMany(mappedBy = "serviceProvider")
    private Set<Appointment> appointments;

    @ManyToOne
    @JoinColumn(name = "branch_id", foreignKey = @ForeignKey(name = "FK_ServiceProvider_Branch"))
    private Branch branch;

    public ServiceProvider() {
    }

    public ServiceProvider(String name, String workingDays, String breakTime, Set<Appointment> appointments, Branch branch) {
        this.name = name;
        this.workingDays = workingDays;
        this.breakTime = breakTime;
        this.appointments = appointments;
        this.branch = branch;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(String workingDays) {
        this.workingDays = workingDays;
    }

    public String getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(String breakTime) {
        this.breakTime = breakTime;
    }

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    // Other fields, constructors, getters and setters
}

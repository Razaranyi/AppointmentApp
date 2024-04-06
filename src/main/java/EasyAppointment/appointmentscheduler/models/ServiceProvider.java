package EasyAppointment.appointmentscheduler.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "service_provider")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProvider {
    @Getter
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

    @Setter
    @Getter
    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @Getter
    @Setter
    @Column(name = "workingDays", nullable = false, columnDefinition = "int[]")
    private int[] workingDays;

    @Setter
    @Getter
    @Column(name = "breakTime", nullable = false, columnDefinition = "TEXT")
    private String breakTime;

    @Setter
    @Getter
    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Appointment> appointments;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "branch_id", foreignKey = @ForeignKey(name = "FK_ServiceProvider_Branch"))
    private Branch branch;

    @Setter
    @Getter
    @OneToOne(mappedBy = "serviceProvider", cascade = CascadeType.ALL)
    private Booking booking;


//    public ServiceProvider (ServiceProviderDTO serviceProviderDTO){
//        this.name = serviceProviderDTO.getName();
//        this.workingDays = serviceProviderDTO.getWorkingDays();
//        this.appointments = serviceProviderDTO.getAppointments();
//        this.breakTime = Arrays.toString(serviceProviderDTO.getBreakTime());
//        this.branch = serviceProviderDTO.getBranch();
//    }
}

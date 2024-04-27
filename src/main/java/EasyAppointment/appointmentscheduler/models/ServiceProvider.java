package EasyAppointment.appointmentscheduler.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @Column(name = "workingDays", nullable = false, columnDefinition = "boolean[]")
    private boolean[] workingDays;

    @Getter
    @Setter
    @Column(name = "breakTime", nullable = false, columnDefinition = "TEXT")
    private String breakHour;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Getter
    @Setter
    @Column(name = "service_provider_image", nullable = true)
    private byte[] serviceProviderImage;

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

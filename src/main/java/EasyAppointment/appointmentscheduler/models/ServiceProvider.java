package EasyAppointment.appointmentscheduler.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * This class represents a service provider in the system.
 * It is a JPA entity class, and its table in the database is named "service_provider".
 * It uses Lombok annotations for automatic generation of getters, setters, constructors, and builder.
 */
@Entity
@Table(name = "service_provider")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ServiceProvider {
    /**
     * The ID of the service provider. It is the primary key in the "service_provider" table.
     * It is generated automatically by a sequence generator named "serviceProvider_sequence".
     */
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

    /**
     * The name of the service provider.
     * It is a required field.
     */
    @Setter
    @Getter
    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    /**
     * The working days of the service provider.
     * It is a required field.
     */
    @Getter
    @Setter
    @Column(name = "workingDays", nullable = false, columnDefinition = "boolean[]")
    private boolean[] workingDays;

    /**
     * The break hour of the service provider.
     * It is a required field.
     */
    @Getter
    @Setter
    @Column(name = "breakTime", nullable = false, columnDefinition = "TEXT")
    private String breakHour;

    /**
     * The session duration of the service provider.
     * It is a required field.
     */
    @Getter
    @Setter
    @Column(name = "session_duration", nullable = false, columnDefinition = "INT")
    private int sessionDuration;

    /**
     * The image of the service provider.
     * It is not a required field.
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Getter
    @Setter
    @Column(name = "service_provider_image", nullable = true)
    private byte[] serviceProviderImage;

    /**
     * The appointments associated with the service provider.
     * One service provider can be associated with many appointments.
     */
    @Setter
    @Getter
    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Appointment> appointments;

    /**
     * The branch associated with the service provider.
     * One service provider can be associated with one branch.
     */
    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "branch_id", foreignKey = @ForeignKey(name = "FK_ServiceProvider_Branch"))
    private Branch branch;

    /**
     * The bookings associated with the service provider.
     * One service provider can be associated with many bookings.
     */
    @Setter
    @Getter
    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL)
    private Set<Booking> bookings;
}
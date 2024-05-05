package EasyAppointment.appointmentscheduler.models;

import EasyAppointment.appointmentscheduler.DTO.BranchDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

/**
 * This class represents a branch in the system.
 * It is a JPA entity class, and its table in the database is named "Branch".
 * It uses Lombok annotations for automatic generation of getters, setters, constructors, and builder.
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class  Branch {
    /**
     * The ID of the branch. It is the primary key in the "Branch" table.
     * It is generated automatically by a sequence generator named "branch_sequence".
     */
    @Id
    @SequenceGenerator(
            name = "branch_sequence",
            sequenceName = "branch_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "branch_sequence")

    @Column(name = "branch_id", updatable = false)
    @Getter
    private Long id;

    /**
     * The name of the branch.
     * It is a required field.
     */
    @Setter
    @Getter
    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    /**
     * The address of the branch.
     * It is a required field.
     */
    @Getter
    @Column(name = "address", nullable = false, columnDefinition = "TEXT")
    private String address;

    /**
     * The closing hours of the branch.
     * It is a required field.
     */
    @Getter
    @Setter
    @Column(name = "ClosingHours", nullable = false)
    private LocalTime closingHours;

    /**
     * The opening hours of the branch.
     * It is a required field.
     */
    @Getter
    @Setter
    @Column(name = "OpeningHours", nullable = false)
    private LocalTime openingHours;

    /**
     * The image of the branch.
     * It is not a required field.
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Getter
    @Setter
    @Column(name = "branch_image", nullable = true)
    private byte[] branchImage;

    /**
     * The business associated with the branch.
     * Many branches can be associated with one business.
     */
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "business_id", foreignKey = @ForeignKey(name = "FK_Business_Branch"))
    private Business business;

    /**
     * The service providers associated with the branch.
     * One branch can be associated with many service providers.
     */
    @Getter
    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ServiceProvider> serviceProviders;

}
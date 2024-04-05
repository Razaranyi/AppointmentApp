package EasyAppointment.appointmentscheduler.models;

import EasyAppointment.appointmentscheduler.DTO.BranchDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class  Branch {
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

    @Setter
    @Getter
    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @Getter
    @Column(name = "address", nullable = false, columnDefinition = "TEXT")
    private String address;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "business_id", foreignKey = @ForeignKey(name = "FK_Business_Branch"))
    private Business business;

    @Getter
    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ServiceProvider> serviceProviders;

    public Branch(String name, String address, Business business, Set<ServiceProvider> serviceProviders) {
        this.name = name;
        this.address = address;
        this.business = business;
        this.serviceProviders = serviceProviders;
    }


}

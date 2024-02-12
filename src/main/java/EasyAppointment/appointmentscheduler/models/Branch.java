package EasyAppointment.appointmentscheduler.models;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;

    @OneToMany(mappedBy = "branch")
    private Set<ServiceProvider> serviceProviders;

    // Constructors, getters, and setters
}

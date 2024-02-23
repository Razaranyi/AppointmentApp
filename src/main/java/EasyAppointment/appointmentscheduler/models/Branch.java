package EasyAppointment.appointmentscheduler.models;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Branch {
    @Id
    @SequenceGenerator(
            name = "branch_sequence",
            sequenceName = "branch_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "branch_sequence")
    @Column(name = "branch_id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;
    @Column(name = "address", nullable = false, columnDefinition = "TEXT")
    private String address;

    @ManyToOne
    @JoinColumn(name = "business_id", foreignKey = @ForeignKey(name = "FK_Business_Branch"))
    private Business business;

    @OneToMany(mappedBy = "branch")
    private Set<ServiceProvider> serviceProviders;

    public Branch() {
    }

    public Branch(String name, String address, Business business, Set<ServiceProvider> serviceProviders) {
        this.name = name;
        this.address = address;
        this.business = business;
        this.serviceProviders = serviceProviders;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public Set<ServiceProvider> getServiceProviders() {
        return serviceProviders;
    }

    public void setServiceProviders(Set<ServiceProvider> serviceProviders) {
        this.serviceProviders = serviceProviders;
    }
}

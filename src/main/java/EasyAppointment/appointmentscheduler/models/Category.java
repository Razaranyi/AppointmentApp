package EasyAppointment.appointmentscheduler.models;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Category {
    @Id
    @SequenceGenerator(
            name = "category_sequence",
            sequenceName = "category_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "category_sequence")
    @Column(name = "category_id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @ManyToMany(mappedBy = "businessCategories")
    private Set<Business> businesses = new HashSet<>();


    public Category() {
    }

    public Category(String name, Set<Business> businesses) {
        this.name = name;
        this.businesses = businesses;
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

    public Set<Business> getBusinesses() {
        return businesses != null ? businesses : Collections.emptySet();
    }

    public void setBusinesses(Set<Business> businesses) {
        this.businesses = businesses;
    }
}

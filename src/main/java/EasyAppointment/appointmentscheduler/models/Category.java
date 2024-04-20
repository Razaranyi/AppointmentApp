package EasyAppointment.appointmentscheduler.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Category {
    @Getter
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

    @Getter
    @Setter
    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @Setter
    @ManyToMany(mappedBy = "businessCategories")
    private Set<Business> businesses = new HashSet<>();


    public Category() {
    }

    public Category(String name, Set<Business> businesses) {
        this.name = name;
        this.businesses = businesses;
    }

    public Category(String category) {
        this.name = category;
    }

    public Set<Business> getBusinesses() {
        return businesses != null ? businesses : Collections.emptySet();
    }

}

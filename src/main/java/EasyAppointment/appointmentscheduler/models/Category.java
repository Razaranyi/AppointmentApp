package EasyAppointment.appointmentscheduler.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a category in the system.
 * It is a JPA entity class, and its table in the database is named "Category".
 */
@Entity
public class Category {
    /**
     * The ID of the category. It is the primary key in the "Category" table.
     * It is generated automatically by a sequence generator named "category_sequence".
     */
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

    /**
     * The name of the category.
     * It is a required field.
     */
    @Getter
    @Setter
    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    /**
     * The businesses associated with the category.
     * One category can be associated with many businesses.
     */
    @Setter
    @ManyToMany(mappedBy = "businessCategories")
    private Set<Business> businesses = new HashSet<>();

    /**
     * Default constructor for the Category class.
     */
    public Category() {
    }

    /**
     * Constructor for the Category class.
     * It initializes the name and businesses of the category.
     * @param name The name of the category.
     * @param businesses The businesses associated with the category.
     */
    public Category(String name, Set<Business> businesses) {
        this.name = name;
        this.businesses = businesses;
    }

    /**
     * Constructor for the Category class.
     * It initializes the name of the category.
     * @param category The name of the category.
     */
    public Category(String category) {
        this.name = category;
    }

    /**
     * Returns the businesses associated with the category.
     * If the businesses are null, it returns an empty set.
     * @return The businesses associated with the category, or an empty set if the businesses are null.
     */
    public Set<Business> getBusinesses() {
        return businesses != null ? businesses : Collections.emptySet();
    }

}
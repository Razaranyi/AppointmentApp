package EasyAppointment.appointmentscheduler.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a business in the system.
 * It is a JPA entity class, and its table in the database is named "Business".
 * It uses Lombok annotations for automatic generation of getters, setters, constructors, and builder.
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Business {
    /**
     * The ID of the business. It is the primary key in the "Business" table.
     * It is generated automatically by a sequence generator named "business_sequence".
     */
    @Id
    @SequenceGenerator(
            name = "business_sequence",
            sequenceName = "business_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "business_sequence"
    )
    @Getter
    @Column(name = "business_id", updatable = false)
    private Long id;

    /**
     * The name of the business.
     * It is a required field.
     */
    @Getter
    @Setter
    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    /**
     * The branches associated with the business.
     * One business can be associated with many branches.
     */
    @Getter
    @Setter
    @OneToMany(mappedBy = "business",cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Branch> branches;

    /**
     * The favorites associated with the business.
     * One business can be associated with many favorites.
     */
    @Getter
    @Setter
    @OneToMany(mappedBy = "business",cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Favorite> favorites = new HashSet<>();

    /**
     * The logo image of the business.
     * It is not a required field.
     */
    @Lob
    @Basic(fetch = FetchType.LAZY) // Lazy load large data
    @Getter
    @Setter
    private byte[] logoImage;

    /**
     * The categories associated with the business.
     * One business can be associated with many categories.
     */
    @ManyToMany
    @JoinTable(
            name = "business_category",
            joinColumns = @JoinColumn(
                    name = "business_id",
                    foreignKey = @ForeignKey(name = "FK_Business_Category")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "category_id",
                    foreignKey = @ForeignKey(name = "FK_Category_Business")
            )
    )
    @Getter
    @Setter
    private Set<Category> businessCategories = new HashSet<>();

    /**
     * The users associated with the business.
     * One business can be associated with many users.
     */
    @OneToMany(mappedBy = "business")
    @Getter
    @Setter
    private Set<User> users = new HashSet<>();
}
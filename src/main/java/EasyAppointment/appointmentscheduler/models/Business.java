package EasyAppointment.appointmentscheduler.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Business {
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

    @Getter
    @Setter
    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @Getter
    @Setter
    @OneToMany(mappedBy = "business",cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Branch> branches;

    @Getter
    @Setter
    @OneToMany(mappedBy = "business")
    private Set<Favorite> favorites = new HashSet<>();

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
    @OneToMany(mappedBy = "business")
    private Set<User> users = new HashSet<>();

    public Business(String name, Set<Category> businessCategories, Set<User> users) {
        this.name = name;
        this.businessCategories = businessCategories;
        this.users = users;
    }
}




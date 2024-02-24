package EasyAppointment.appointmentscheduler.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
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
    @Column(name = "business_id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @OneToMany(mappedBy = "business")
    private Set<Branch> branches;

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
    private Set<Category> businessCategories = new HashSet<>();
    @OneToMany(mappedBy = "business")
    private Set<User> users = new HashSet<>();

    public Business() {

    }

    public Business(Long id, String name, Set<Category> businessCategories, Set<User> users) {
        this.id = id;
        this.name = name;
        this.businessCategories = businessCategories;
        this.users = users;
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

}

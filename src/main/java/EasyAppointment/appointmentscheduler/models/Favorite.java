package EasyAppointment.appointmentscheduler.models;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Favorite {
    @Id
    @SequenceGenerator(
            name = "favorite_sequence",
            sequenceName = "favorite_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "favorite_sequence"
    )

    @Column(name = "favorite_id", updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_Favorite_User"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "business_id", foreignKey = @ForeignKey(name = "FK_Favorite_Business"))
    private Business business;

    public Favorite() {
    }

    public Favorite(User user, Business business) {
        this.user = user;
        this.business = business;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }
}

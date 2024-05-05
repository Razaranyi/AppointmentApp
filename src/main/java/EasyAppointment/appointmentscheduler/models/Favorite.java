package EasyAppointment.appointmentscheduler.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/**
 * This class represents a favorite in the system.
 * It is a JPA entity class, and its table in the database is named "Favorite".
 * It uses Lombok annotations for automatic generation of getters, setters, constructors, and builder.
 */
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Favorite {
    /**
     * The ID of the favorite. It is the primary key in the "Favorite" table.
     * It is generated automatically by a sequence generator named "favorite_sequence".
     */
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

    /**
     * The user associated with the favorite.
     * Many favorites can be associated with one user.
     */
    @ManyToOne
    @Setter
    @JsonIgnore
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_Favorite_User"))
    private User user;

    /**
     * The business associated with the favorite.
     * Many favorites can be associated with one business.
     */
    @ManyToOne
    @Setter
    @JoinColumn(name = "business_id", foreignKey = @ForeignKey(name = "FK_Favorite_Business"))
    private Business business;
}
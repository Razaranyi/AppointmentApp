package EasyAppointment.appointmentscheduler.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
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
    @Setter
    @JsonIgnore
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_Favorite_User"))
    private User user;

    @ManyToOne
    @Setter
    @JoinColumn(name = "business_id", foreignKey = @ForeignKey(name = "FK_Favorite_Business"))
    private Business business;


}




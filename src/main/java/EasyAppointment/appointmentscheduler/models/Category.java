package EasyAppointment.appointmentscheduler.models;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "categories")
    private Set<Business> businesses;

    // Constructors, getters, and setters
}

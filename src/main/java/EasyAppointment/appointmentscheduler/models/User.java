package EasyAppointment.appointmentscheduler.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.GenerationType.SEQUENCE;

/**
 * This class represents a user in the system.
 * It is a JPA entity class, and its table in the database is named "users".
 * It uses Lombok annotations for automatic generation of getters, setters, constructors, and builder.
 * It implements UserDetails interface for Spring Security.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "User")
@Table(name = "users")
public class User implements UserDetails {
    /**
     * The ID of the user. It is the primary key in the "users" table.
     * It is generated automatically by a sequence generator named "user_sequence".
     */
    @Getter
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "user_sequence"
    )
    @Column(name = "user_id", updatable = false)
    private Long id;

    /**
     * The full name of the user.
     * It is a required field.
     */
    @Setter
    @Getter
    @Column(name = "full_name", nullable = false, columnDefinition = "TEXT")
    private String fullName;

    /**
     * The email of the user.
     * It is a required field and must be unique.
     * It is also validated to be a valid email.
     */
    @Setter
    @Getter
    @Email(message = "Email is not valid")
    @Column(name = "email", nullable = false, columnDefinition = "TEXT", unique = true)
    private String email;

    /**
     * The password of the user.
     * It is a required field.
     */
    @Getter
    @Setter
    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    private String password;

    /**
     * The role of the user.
     * It is an enum value.
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * The bookings associated with the user.
     * One user can have many bookings.
     */
    @Getter
    @Setter
    @OneToMany(mappedBy = "user")
    private Set<Booking> bookings;

    /**
     * The favorites associated with the user.
     * One user can have many favorites.
     */
    @OneToMany(mappedBy = "user")
    private Set<Favorite> favorites = new HashSet<>();

    /**
     * The business associated with the user.
     * One user can be associated with one business.
     */
    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", foreignKey = @ForeignKey(name = "FK_Business_User"))
    private Business business;

    /**
     * Constructor for the User class.
     * It initializes the full name, email, and password of the user.
     * @param fullName The full name of the user.
     * @param email The email of the user.
     * @param encode The password of the user.
     */
    public User(String fullName, String email, String encode) {
        this.fullName = fullName;
        this.email = email;
        this.password = encode;
    }

    /**
     * Returns the authorities associated with the user.
     * The authorities are determined by the role of the user.
     * @return A collection of authorities associated with the user.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    /**
     * Returns the username of the user.
     * In this case, the username is the email of the user.
     * @return The username of the user.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Returns the password of the user.
     * @return The password of the user.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns whether the user's account has not expired.
     * @return true, because the user's account has not expired.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Returns whether the user's account is not locked.
     * @return true, because the user's account is not locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Returns whether the user's credentials have not expired.
     * @return true, because the user's credentials have not expired.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Returns whether the user is enabled.
     * @return true, because the user is enabled.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Adds a favorite to the user's favorites.
     * @param savedFavorite The favorite to be added.
     */
    public void setFavorite(Favorite savedFavorite) {
        favorites.add(savedFavorite);
    }
}
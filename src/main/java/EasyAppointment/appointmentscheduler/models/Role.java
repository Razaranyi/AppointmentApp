package EasyAppointment.appointmentscheduler.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static EasyAppointment.appointmentscheduler.models.Permission.*;

/**
 * This enum represents the different roles in the system.
 * Each role is associated with a specific set of permissions.
 * It uses Lombok annotations for automatic generation of getters and required constructor.
 */
@RequiredArgsConstructor
public enum Role {
    /**
     * Represents the user role.
     * The user role has permissions to read, write, delete, and create user data.
     */
    USER(
            Set.of(
                    USER_READ,
                    USER_WRITE,
                    USER_DELETE,
                    USER_CREATE
            )
    ),
    /**
     * Represents the admin role.
     * The admin role has permissions to read, write, delete, and create admin data.
     */
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_WRITE,
                    ADMIN_DELETE,
                    ADMIN_CREATE
            )
    );

    /**
     * The set of permissions associated with the role.
     */
    @Getter
    private final Set<Permission> permissions;

    /**
     * Returns a list of authorities associated with the role.
     * Each authority is represented by a SimpleGrantedAuthority object.
     * The authorities include the permissions associated with the role and the role itself.
     * @return A list of authorities associated with the role.
     */
    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

}
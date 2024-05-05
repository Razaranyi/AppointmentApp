package EasyAppointment.appointmentscheduler.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This enum represents the different permissions in the system.
 * Each permission is associated with a specific string value.
 * It uses Lombok annotations for automatic generation of getters and required constructor.
 */
@RequiredArgsConstructor
public enum Permission {
    /**
     * Represents the permission to read admin data.
     */
    ADMIN_READ("admin:read"),

    /**
     * Represents the permission to write admin data.
     */
    ADMIN_WRITE("admin:write"),

    /**
     * Represents the permission to delete admin data.
     */
    ADMIN_DELETE("admin:delete"),

    /**
     * Represents the permission to create admin data.
     */
    ADMIN_CREATE("admin:create"),

    /**
     * Represents the permission to read user data.
     */
    USER_READ("user:read"),

    /**
     * Represents the permission to write user data.
     */
    USER_WRITE("user:write"),

    /**
     * Represents the permission to delete user data.
     */
    USER_DELETE("user:delete"),

    /**
     * Represents the permission to create user data.
     */
    USER_CREATE("user:create");

    /**
     * The string value associated with the permission.
     */
    @Getter
    private final String permission;

}
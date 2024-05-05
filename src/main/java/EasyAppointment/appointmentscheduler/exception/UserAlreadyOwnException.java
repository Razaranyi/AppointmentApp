package EasyAppointment.appointmentscheduler.exception;

/**
 * This is a custom exception class for the scenario when a user already owns a business or a branch or a service provider.
 * It extends the IllegalStateException class, meaning it's an unchecked exception.
 * Unchecked exceptions are exceptions that can be programmatically avoided.
 */
public class UserAlreadyOwnException extends IllegalStateException {
    /**
     * This constructor is used to create a new UserAlreadyOwnException with a default message.
     * The default message is "User already owns a business".
     */
    public UserAlreadyOwnException() {
        super("User already owns a business");
    }

    /**
     * This constructor is used to create a new UserAlreadyOwnException.
     * It takes a message as a parameter, which describes the details of the exception.
     * This message can be retrieved later by calling getMessage() method on the exception object.
     * @param s The detail message for the exception.
     */
    public UserAlreadyOwnException(String s) {
        super(s);
    }
}
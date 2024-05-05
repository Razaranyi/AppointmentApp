package EasyAppointment.appointmentscheduler.exception;

/**
 * This is a custom exception class for the scenario when a user already exists.
 * It extends the Exception class, meaning it's a checked exception.
 * Checked exceptions are exceptions that need to be declared in a method or constructor's throws clause if they can be thrown by the execution of the method or constructor and propagate outside the method or constructor boundary.
 */
public class UserAlreadyExistException extends Exception {
    /**
     * This constructor is used to create a new UserAlreadyExistException.
     * It takes a message as a parameter, which describes the details of the exception.
     * This message can be retrieved later by calling getMessage() method on the exception object.
     * @param message The detail message for the exception.
     */
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
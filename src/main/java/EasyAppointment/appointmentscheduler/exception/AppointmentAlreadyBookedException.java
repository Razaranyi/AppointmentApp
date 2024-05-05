package EasyAppointment.appointmentscheduler.exception;

/**
 * This is a custom exception class for the scenario when an appointment is already booked.
 * It extends the RuntimeException class, meaning it's an unchecked exception.
 * Unchecked exceptions are exceptions that can be programmatically avoided.
 */
public class AppointmentAlreadyBookedException extends RuntimeException{
    /**
     * This constructor is used to create a new AppointmentAlreadyBookedException.
     * It takes a message as a parameter, which describes the details of the exception.
     * This message can be retrieved later by calling getMessage() method on the exception object.
     * @param message The detail message for the exception.
     */
    public AppointmentAlreadyBookedException(String message) {
        super(message);
    }
}
package EasyAppointment.appointmentscheduler.exception;

public class FavoriteAlreadyExistsException extends RuntimeException{
    public FavoriteAlreadyExistsException(String message) {
        super(message);
    }
}

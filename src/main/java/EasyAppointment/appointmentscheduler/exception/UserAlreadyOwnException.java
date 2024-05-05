package EasyAppointment.appointmentscheduler.exception;

public class UserAlreadyOwnException extends IllegalStateException {
    public UserAlreadyOwnException() {
        super("User already owns a business");
    }

    public UserAlreadyOwnException(String s) {
        super(s);
    }
}

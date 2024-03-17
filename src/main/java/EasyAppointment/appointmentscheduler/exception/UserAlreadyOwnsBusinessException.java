package EasyAppointment.appointmentscheduler.exception;

public class UserAlreadyOwnsBusinessException extends IllegalStateException {
    public UserAlreadyOwnsBusinessException() {
        super("User already owns a business");
    }

    public UserAlreadyOwnsBusinessException(String s) {
        super(s);
    }
}

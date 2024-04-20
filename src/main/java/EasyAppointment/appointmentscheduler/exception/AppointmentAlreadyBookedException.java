package EasyAppointment.appointmentscheduler.exception;

public class AppointmentAlreadyBookedException extends RuntimeException{
    public AppointmentAlreadyBookedException(String message) {
        super(message);
    }
}

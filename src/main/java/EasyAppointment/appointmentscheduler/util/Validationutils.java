package EasyAppointment.appointmentscheduler.util;

import java.time.LocalTime;

public class Validationutils {
    public static boolean isOpeningTimeBeforeClosingTime(LocalTime openingTime, LocalTime closingTime) {
        if (openingTime == null || closingTime == null) {
            return false; // Consider how you want to handle null values
        }
        return openingTime.isBefore(closingTime);
    }

}

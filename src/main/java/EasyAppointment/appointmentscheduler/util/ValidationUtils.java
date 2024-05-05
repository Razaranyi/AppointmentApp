package EasyAppointment.appointmentscheduler.util;

import java.time.LocalTime;
/**
 * This class provides utility methods for validation.
 */
public class ValidationUtils {

    /**
     * This method checks if the opening time is before the closing time.
     * It returns false if either the opening time or the closing time is null.
     * @param openingTime The opening time to be checked.
     * @param closingTime The closing time to be checked.
     * @return A boolean indicating whether the opening time is before the closing time.
     */
    public static boolean isOpeningTimeBeforeClosingTime(LocalTime openingTime, LocalTime closingTime) {
        if (openingTime == null || closingTime == null) {
            return false;
        }
        return openingTime.isBefore(closingTime);
    }

}

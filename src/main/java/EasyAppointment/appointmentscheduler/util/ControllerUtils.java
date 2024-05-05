package EasyAppointment.appointmentscheduler.util;

import org.springframework.validation.BindingResult;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import java.util.stream.Collectors;

/**
 * This class provides utility methods for controllers.
 */
public class ControllerUtils {

    /**
     * This method retrieves error messages from a BindingResult object.
     * It uses Java 8's Stream API to process the errors.
     * @param result The BindingResult object containing the validation results.
     * @return A string containing all error messages, separated by commas.
     */
    public static String getErrorMessages(BindingResult result) {
        return result.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
    }
}
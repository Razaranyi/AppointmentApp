package EasyAppointment.appointmentscheduler.DTO;

import EasyAppointment.appointmentscheduler.models.Appointment;
import EasyAppointment.appointmentscheduler.models.ServiceProvider;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is a data transfer object (DTO) for ServiceProvider.
 * It is used to send data over the network or between processes.
 * It includes all the necessary information about a service provider.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ServiceProviderDTO implements DTOInterface{
    /**
     * The ID of the service provider.
     */
    private Long id;

    /**
     * The name of the service provider.
     * It must not be blank and must contain only valid characters.
     */
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[\\u0590-\\u05FF\\uFB1D-\\uFB4F A-Za-z-\\s']+$", message = "Name must contain only valid characters")
    private String name;

    /**
     * The working days of the service provider.
     * It must not be null.
     */
    @NotNull(message = "Working days are required")
    private boolean[] workingDays;

    /**
     * The break hours of the service provider.
     * It must not be null.
     */
    @NotNull(message = "Break hour is required")
    private String[] breakHour;

    /**
     * The session duration of the service provider.
     * It must be at least 15.
     */
    @Min(value = 15, message = "Session duration is required")
    private int sessionDuration;

    /**
     * The IDs of the appointments of the service provider.
     */
    private Set<Long> appointmentsIds;

    /**
     * The ID of the branch that the service provider belongs to.
     */
    private Long branchId;

    /**
     * The image of the service provider.
     * It must not be null.
     */
    @NotNull(message = "Service Provider image is required")
    private byte[] serviceProviderImage;

    /**
     * This constructor is used to create a ServiceProviderDTO from a ServiceProvider object.
     * It copies all the necessary information from the ServiceProvider object to the ServiceProviderDTO.
     * @param serviceProvider The ServiceProvider object to be converted into a ServiceProviderDTO.
     */
    public ServiceProviderDTO(ServiceProvider serviceProvider){
        this.id = serviceProvider.getId();
        this.name = serviceProvider.getName();
        this.workingDays = serviceProvider.getWorkingDays();
        if (serviceProvider.getBreakHour() != null){
            this.breakHour = serviceProvider.getBreakHour().split("-");
        }
        this.sessionDuration = serviceProvider.getSessionDuration();
        this.branchId = serviceProvider.getBranch().getId();
        if (serviceProvider.getAppointments() != null) {
            this.appointmentsIds = serviceProvider.getAppointments().stream().map(Appointment::getId).collect(Collectors.toSet());
        }
        if (serviceProvider.getServiceProviderImage() != null) {
            this.serviceProviderImage = serviceProvider.getServiceProviderImage();
        }
    }

    /**
     * This method is used to get the break hours of the service provider.
     * @return The break hours of the service provider.
     */
    public String[] getBreakHour() {
        if (breakHour == null) {
            return null;
        }
        int length = breakHour.length;
        System.out.println("length: " + length);

        return breakHour;
    }
}
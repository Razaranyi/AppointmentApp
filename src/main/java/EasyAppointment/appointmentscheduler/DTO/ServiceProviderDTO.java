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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ServiceProviderDTO implements DTOInterface{
    private Long id;
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[\\u0590-\\u05FF\\uFB1D-\\uFB4F A-Za-z-\\s']+$", message = "Name must contain only valid characters")
    private String name;
    @NotNull(message = "Working days are required")
    private boolean[] workingDays;
    @NotNull(message = "Break hour is required")
    private String[] breakHour;
    @Min(value = 15, message = "Session duration is required")
    private int sessionDuration;
    private Set<Long> appointmentsIds;
    private Long branchId;

    @NotNull(message = "Service Provider image is required")
    private byte[] serviceProviderImage;



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

    public String[] getBreakHour() {
    if (breakHour == null) {
        return null;
    }
    int length = breakHour.length;
    System.out.println("length: " + length);

    return breakHour;
    }
}

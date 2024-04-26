package EasyAppointment.appointmentscheduler.DTO;

import EasyAppointment.appointmentscheduler.models.ServiceProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceProviderDTO implements DTOInterface{
    private Long id;
    private String name;
    private int[] workingDays;
    private String[] breakTime;
    private Set<AppointmentDTO> appointmentsDTO;
    private Long branchId;
    private byte[] serviceProviderImage;



    public ServiceProviderDTO(ServiceProvider serviceProvider){
        this.id = serviceProvider.getId();
        this.name = serviceProvider.getName();
        this.workingDays = serviceProvider.getWorkingDays();
        this.breakTime = serviceProvider.getBreakHour().split("-");
        this.branchId = serviceProvider.getBranch().getId();
        if (serviceProvider.getAppointments() != null) {
            this.appointmentsDTO = serviceProvider.getAppointments().stream()
                    .map(AppointmentDTO::new)
                    .collect(Collectors.toSet());
        }
        if (serviceProvider.getServiceProviderImage() != null) {
            this.serviceProviderImage = serviceProvider.getServiceProviderImage();
        }
    }
}

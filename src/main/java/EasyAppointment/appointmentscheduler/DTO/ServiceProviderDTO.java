package EasyAppointment.appointmentscheduler.DTO;

import EasyAppointment.appointmentscheduler.models.Appointment;
import EasyAppointment.appointmentscheduler.models.ServiceProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceProviderDTO implements DTOInterface{
    private Long id;
    private String name;
    private int[] workingDays;
    private String[] breakTime;
    private Set<Appointment> appointments;
    private Long branchId;


    public ServiceProviderDTO(ServiceProvider serviceProvider){
        this.id = serviceProvider.getId();
        this.name = serviceProvider.getName();
        this.workingDays = serviceProvider.getWorkingDays();
        this.appointments = serviceProvider.getAppointments();
        this.breakTime = serviceProvider.getBreakTime().split("-");
        this.branchId = serviceProvider.getBranch().getId();
    }
}
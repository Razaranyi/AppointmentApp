package EasyAppointment.appointmentscheduler.DTO;

import EasyAppointment.appointmentscheduler.models.Branch;
import EasyAppointment.appointmentscheduler.models.ServiceProvider;
import lombok.*;

import java.time.LocalTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class BranchDTO implements DTOInterface{
    private Long BranchId;
    private String name;
    private String address;
    private LocalTime closingHours;
    private LocalTime openingHours;
    private Long businessId;
    private Set<ServiceProvider> serviceProviders; //consider to take only ids/names


    public BranchDTO(Branch branch){
        this.BranchId = branch.getId();
        this.name = branch.getName();
        this.address = branch.getAddress();
        this.businessId = branch.getBusiness().getId();
        this.serviceProviders = branch.getServiceProviders();
        this.closingHours = branch.getClosingHours();
        this.openingHours = branch.getOpeningHours();
    }

}

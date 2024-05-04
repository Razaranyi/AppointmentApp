package EasyAppointment.appointmentscheduler.DTO;

import EasyAppointment.appointmentscheduler.models.Branch;
import EasyAppointment.appointmentscheduler.models.ServiceProvider;
import EasyAppointment.appointmentscheduler.util.Validationutils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@ToString
public class BranchDTO implements DTOInterface{


    private Long branchId;


    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[\\u0590-\\u05FF\\uFB1D-\\uFB4F A-Za-z-\\s']+$", message = "Name must contain only valid characters")
    private String name;

    private String address;

    @NotNull(message = "Closing hours are required")
    private LocalTime closingHours;

    @NotNull(message = "Opening hours are required")
    private LocalTime openingHours;

    private Long businessId;
    @NotNull(message = "Branch image is required")
    private byte[] branchImage;
    private Set<Long> serviceProvidersIds;


    public BranchDTO(Branch branch){
        this.branchId = branch.getId();
        this.name = branch.getName();
        this.address = branch.getAddress();
        this.businessId = branch.getBusiness().getId();
        this.serviceProvidersIds = branch.getServiceProviders().stream().map(ServiceProvider::getId).collect(java.util.stream.Collectors.toSet());

        if (!Validationutils.isOpeningTimeBeforeClosingTime(branch.getOpeningHours(), branch.getClosingHours())) {
            throw new IllegalArgumentException("Opening time must be before closing time");
        }
        this.closingHours = branch.getClosingHours();
        this.openingHours = branch.getOpeningHours();
        if (branch.getBranchImage() != null) {
            this.branchImage = branch.getBranchImage();
        }
    }
}

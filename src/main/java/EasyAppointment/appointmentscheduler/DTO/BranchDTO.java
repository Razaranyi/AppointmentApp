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

/**
 * This is a data transfer object (DTO) for Branch.
 * It is used to send data over the network or between processes.
 * It includes all the necessary information about a branch.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@ToString
public class BranchDTO implements DTOInterface{

    /**
     * The ID of the branch.
     */
    private Long branchId;

    /**
     * The name of the branch.
     * It must not be blank and must contain only valid characters.
     */
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[\\u0590-\\u05FF\\uFB1D-\\uFB4F A-Za-z-\\s']+$", message = "Name must contain only valid characters")
    private String name;

    /**
     * The address of the branch.
     */
    private String address;

    /**
     * The closing hours of the branch.
     * It must not be null.
     */
    @NotNull(message = "Closing hours are required")
    private LocalTime closingHours;

    /**
     * The opening hours of the branch.
     * It must not be null.
     */
    @NotNull(message = "Opening hours are required")
    private LocalTime openingHours;

    /**
     * The ID of the business that the branch belongs to.
     */
    private Long businessId;

    /**
     * The image of the branch.
     * It must not be null.
     */
    @NotNull(message = "Branch image is required")
    private byte[] branchImage;

    /**
     * The IDs of the service providers that work at the branch.
     */
    private Set<Long> serviceProvidersIds;

    /**
     * This constructor is used to create a BranchDTO from a Branch object.
     * It copies all the necessary information from the Branch object to the BranchDTO.
     * @param branch The Branch object to be converted into a BranchDTO.
     */
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
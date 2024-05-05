package EasyAppointment.appointmentscheduler.DTO;

import EasyAppointment.appointmentscheduler.models.Business;
import EasyAppointment.appointmentscheduler.models.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is a data transfer object (DTO) for Business.
 * It is used to send data over the network or between processes.
 * It includes all the necessary information about a business.
 */
@NoArgsConstructor
@Getter
@Setter
public class BusinessDTO implements DTOInterface {

    /**
     * The ID of the business.
     */
    private Long id;

    /**
     * The name of the business.
     * It must not be blank and must contain only valid characters.
     */
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[\\u0590-\\u05FF\\uFB1D-\\uFB4F A-Za-z-\\s']+$", message = "Name must contain only valid characters")
    private String name;

    /**
     * The categories of the business.
     */
    private Set<String> businessCategories;

    /**
     * The logo image of the business.
     * It must not be null.
     */
    @NotNull(message = "Logo image is required")
    private byte[] logoImage;

    /**
     * This constructor is used to create a BusinessDTO from a Business object.
     * It copies all the necessary information from the Business object to the BusinessDTO.
     * @param business The Business object to be converted into a BusinessDTO.
     */
    public BusinessDTO(Business business) {
        this.id = business.getId();
        this.name = business.getName();
        this.businessCategories = business.getBusinessCategories().stream().map(Category::getName).collect(Collectors.toSet());
        this.logoImage = business.getLogoImage();
    }

    /**
     * This constructor is used to create a BusinessDTO with the provided parameters.
     * @param id The ID of the business.
     * @param name The name of the business.
     * @param businessCategories The categories of the business.
     */
    public BusinessDTO(Long id, String name, Set<Category> businessCategories) {
        this.id = id;
        this.name = name;
        this.businessCategories = businessCategories.stream().map(Category::getName).collect(Collectors.toSet());
    }

    /**
     * This method is used to get a string representation of the BusinessDTO.
     * @return A string representation of the BusinessDTO.
     */
    @Override
    public String toString() {
        return "BusinessDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", businessCategories=" + businessCategories +
                ", logoImage=" + Arrays.toString(logoImage) +
                '}';
    }
}
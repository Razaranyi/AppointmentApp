package EasyAppointment.appointmentscheduler.DTO;

import EasyAppointment.appointmentscheduler.models.Business;
import EasyAppointment.appointmentscheduler.models.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;


@NoArgsConstructor
@Getter
@Setter
public class BusinessDTO implements DTOInterface {

    private Long id;

    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[\\u0590-\\u05FF\\uFB1D-\\uFB4F A-Za-z-\\s']+$", message = "Name must contain only valid characters")
    private String name;

    private Set<String> businessCategories;
    private byte[] logoImage;

    public BusinessDTO(Business business) {
        this.id = business.getId();
        this.name = business.getName();
        this.businessCategories = business.getBusinessCategories().stream().map(Category::getName).collect(Collectors.toSet());
        this.logoImage = business.getLogoImage();
    }

    public BusinessDTO(Long id, String name, Set<Category> businessCategories) {
        this.id = id;
        this.name = name;
        this.businessCategories = businessCategories.stream().map(Category::getName).collect(Collectors.toSet());
    }

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

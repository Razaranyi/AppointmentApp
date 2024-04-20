package EasyAppointment.appointmentscheduler.DTO;

import EasyAppointment.appointmentscheduler.models.Business;
import EasyAppointment.appointmentscheduler.models.Category;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BusinessDTO implements DTOInterface {

    private Long id;
    private String name;
    private Set<Category> businessCategories;
    private byte[] logoImage;

    public BusinessDTO(Business business) {
        this.id = business.getId();
        this.name = business.getName();
        this.businessCategories = business.getBusinessCategories();
        this.logoImage = business.getLogoImage();
    }

    public BusinessDTO(Long id, String name, Set<Category> businessCategories) {
        this.id = id;
        this.name = name;
        this.businessCategories = businessCategories;
    }
}

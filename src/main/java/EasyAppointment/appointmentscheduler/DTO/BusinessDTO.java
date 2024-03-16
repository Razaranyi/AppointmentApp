package EasyAppointment.appointmentscheduler.DTO;

import EasyAppointment.appointmentscheduler.models.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor

public class BusinessDTO {
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private Set<Category> businessCategories;

}

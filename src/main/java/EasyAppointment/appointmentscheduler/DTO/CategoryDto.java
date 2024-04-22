package EasyAppointment.appointmentscheduler.DTO;

import EasyAppointment.appointmentscheduler.models.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryDto implements DTOInterface{
    private Long id;
    private String name;

    public CategoryDto(Category category){
        this.id = category.getId();
        this.name = category.getName();
    }

}

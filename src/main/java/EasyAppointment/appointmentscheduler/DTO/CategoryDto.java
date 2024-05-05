package EasyAppointment.appointmentscheduler.DTO;

import EasyAppointment.appointmentscheduler.models.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class CategoryDto implements DTOInterface{
    private Long id;
    private String name;
    private Set<Long> businessIds;

    public CategoryDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.businessIds = category.getBusinesses().stream().map(business -> business.getId()).collect(java.util.stream.Collectors.toSet());
    }
}

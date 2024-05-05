package EasyAppointment.appointmentscheduler.DTO;

import EasyAppointment.appointmentscheduler.models.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * This is a data transfer object (DTO) for Category.
 * It is used to send data over the network or between processes.
 * It includes all the necessary information about a category.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryDto implements DTOInterface{
    /**
     * The ID of the category.
     */
    private Long id;

    /**
     * The name of the category.
     */
    private String name;

    /**
     * The IDs of the businesses that belong to this category.
     */
    private Set<Long> businessIds;

    /**
     * This constructor is used to create a CategoryDto from a Category object.
     * It copies all the necessary information from the Category object to the CategoryDto.
     * @param category The Category object to be converted into a CategoryDto.
     */
    public CategoryDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.businessIds = category.getBusinesses().stream().map(business -> business.getId()).collect(java.util.stream.Collectors.toSet());
    }
}
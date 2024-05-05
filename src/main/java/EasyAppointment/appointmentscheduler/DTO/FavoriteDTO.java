package EasyAppointment.appointmentscheduler.DTO;

import EasyAppointment.appointmentscheduler.models.Favorite;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This is a data transfer object (DTO) for Favorite.
 * It is used to send data over the network or between processes.
 * It includes all the necessary information about a favorite.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteDTO implements DTOInterface {
    /**
     * The ID of the favorite.
     */
    private Long favoriteId;

    /**
     * The ID of the user who marked the business as a favorite.
     */
    private Long userId;

    /**
     * The ID of the business that was marked as a favorite.
     */
    private Long businessId;

    /**
     * This constructor is used to create a FavoriteDTO from a Favorite object.
     * It copies all the necessary information from the Favorite object to the FavoriteDTO.
     * @param favorite The Favorite object to be converted into a FavoriteDTO.
     */
    public FavoriteDTO(Favorite favorite) {
        this.favoriteId = favorite.getId();
        this.userId = favorite.getUser().getId();
        this.businessId = favorite.getBusiness().getId();
    }

}
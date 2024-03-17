package EasyAppointment.appointmentscheduler.DTO;

import EasyAppointment.appointmentscheduler.models.Favorite;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteDTO implements DTOInterface {
    private Long favoriteId;
    private Long userId;
    private Long businessId;

    public FavoriteDTO(Favorite favorite) {
        this.favoriteId = favorite.getId();
        this.userId = favorite.getUser().getId();
        this.businessId = favorite.getBusiness().getId();
    }

}



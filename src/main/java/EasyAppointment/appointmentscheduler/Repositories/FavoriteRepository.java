package EasyAppointment.appointmentscheduler.Repositories;

import EasyAppointment.appointmentscheduler.models.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
}

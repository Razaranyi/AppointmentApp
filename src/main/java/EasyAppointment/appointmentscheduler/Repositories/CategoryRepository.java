package EasyAppointment.appointmentscheduler.Repositories;

import EasyAppointment.appointmentscheduler.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

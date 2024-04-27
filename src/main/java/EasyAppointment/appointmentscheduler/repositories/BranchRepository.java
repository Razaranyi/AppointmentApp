package EasyAppointment.appointmentscheduler.repositories;

import EasyAppointment.appointmentscheduler.models.Branch;
import EasyAppointment.appointmentscheduler.models.Business;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    List<Branch> findByBusiness(Business business);

    Optional<Object> findByNameAndBusiness(String branchName, Business business);
}

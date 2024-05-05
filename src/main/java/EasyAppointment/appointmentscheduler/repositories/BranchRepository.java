package EasyAppointment.appointmentscheduler.repositories;

import EasyAppointment.appointmentscheduler.models.Branch;
import EasyAppointment.appointmentscheduler.models.Business;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * This interface represents the repository for the Branch entity.
 * It extends JpaRepository to provide CRUD operations for the Branch entity.
 * It also declares custom query methods to find branches by business, by name and business, and to check if a branch exists by name and business.
 */
public interface BranchRepository extends JpaRepository<Branch, Long> {
    /**
     * Finds branches by business.
     * @param business The business entity.
     * @return A list of branches that belong to the specified business.
     */
    List<Branch> findByBusiness(Business business);

    /**
     * Finds a branch by name and business.
     * @param branchName The name of the branch.
     * @param business The business entity.
     * @return An optional branch that matches the specified name and business.
     */
    Optional<Object> findByNameAndBusiness(String branchName, Business business);

    /**
     * Checks if a branch exists by name and business.
     * @param name The name of the branch.
     * @param business The business entity.
     * @return true if a branch exists that matches the specified name and business, false otherwise.
     */
    boolean existsByNameAndBusiness(String name, Business business);
}
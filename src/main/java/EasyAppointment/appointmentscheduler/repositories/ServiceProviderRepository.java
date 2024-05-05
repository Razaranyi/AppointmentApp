package EasyAppointment.appointmentscheduler.repositories;

import EasyAppointment.appointmentscheduler.models.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface represents the repository for the ServiceProvider entity.
 * It extends JpaRepository to provide CRUD operations for the ServiceProvider entity.
 * It also declares a custom query method to check if a service provider exists by name and branch ID.
 */
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {

    /**
     * Checks if a service provider exists by name and branch ID.
     * @param name The name of the service provider.
     * @param branchId The ID of the branch.
     * @return true if a service provider exists that matches the specified name and branch ID, false otherwise.
     */
    boolean existsByNameAndBranchId(String name, Long branchId);
}
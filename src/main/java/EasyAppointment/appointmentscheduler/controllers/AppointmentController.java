package EasyAppointment.appointmentscheduler.controllers;

import EasyAppointment.appointmentscheduler.DTO.AppointmentDTO;
import EasyAppointment.appointmentscheduler.services.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * This is the controller for the Appointment entity.
 * It handles HTTP requests and responses related to Appointment operations.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/business/{businessId}/branch/{branchId}/service-provider/{serviceProviderId}/appointment")
public class AppointmentController {
    private final AppointmentService appointmentService;

    /**
     * This method handles the GET request to retrieve appointments by page for a specific service provider.
     * @param page The page number.
     * @param serviceProviderId The ID of the service provider.
     * @param branchId The ID of the branch.
     * @param businessId The ID of the business.
     * @return ResponseEntity containing List of AppointmentDTO
     */
    @GetMapping("/get/page/{page}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentByPage(@PathVariable("page") int page, @PathVariable("serviceProviderId") Long serviceProviderId, @PathVariable String branchId, @PathVariable String businessId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByServiceProviderIdForWeek(serviceProviderId, page));
    }

    /**
     * This method handles the GET request to retrieve appointments by date for a specific service provider.
     * @param date The date.
     * @param serviceProviderId The ID of the service provider.
     * @param branchId The ID of the branch.
     * @param businessId The ID of the business.
     * @return ResponseEntity containing List of AppointmentDTO
     */
    @GetMapping("/get/date/{date}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentByDate(@PathVariable("date") String date, @PathVariable("serviceProviderId") Long serviceProviderId, @PathVariable String branchId, @PathVariable String businessId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByServiceProviderIdForDay(serviceProviderId, LocalDate.parse(date)));
    }
}
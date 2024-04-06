package EasyAppointment.appointmentscheduler.controllers;

import EasyAppointment.appointmentscheduler.DTO.AppointmentDTO;
import EasyAppointment.appointmentscheduler.services.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/busines/{businessId}/branches/{branchId}/serviceProvider/{serviceProviderId}/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping("/get/{page}")
    public ResponseEntity<List<AppointmentDTO>> getAppointment(@PathVariable("page") int page, @PathVariable("serviceProviderId") Long serviceProviderId, @PathVariable String branchId, @PathVariable String businessId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByServiceProviderIdForWeek(serviceProviderId, page)
        );
    }
}

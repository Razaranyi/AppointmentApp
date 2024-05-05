package EasyAppointment.appointmentscheduler.controllers;

import EasyAppointment.appointmentscheduler.DTO.AppointmentDTO;
import EasyAppointment.appointmentscheduler.services.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/business/{businessId}/branch/{branchId}/service-provider/{serviceProviderId}/appointment")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping("/get/page/{page}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentByPage(@PathVariable("page") int page, @PathVariable("serviceProviderId") Long serviceProviderId, @PathVariable String branchId, @PathVariable String businessId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByServiceProviderIdForWeek(serviceProviderId, page));
    }

    @GetMapping("/get/date/{date}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentByDate(@PathVariable("date") String date, @PathVariable("serviceProviderId") Long serviceProviderId, @PathVariable String branchId, @PathVariable String businessId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByServiceProviderIdForDay(serviceProviderId, LocalDate.parse(date)));
    }


}

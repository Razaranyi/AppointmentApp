package EasyAppointment.appointmentscheduler.services;


import EasyAppointment.appointmentscheduler.DTO.AppointmentDTO;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    public ApiResponse<AppointmentDTO> getAllAppointmentsByServiceProviderIdAndDate(Long serviceProviderId, LocalDateTime date) {
        return null;
    }
}

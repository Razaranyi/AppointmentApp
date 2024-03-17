package EasyAppointment.appointmentscheduler.requestsAndResponses;

import EasyAppointment.appointmentscheduler.DTO.DTOInterface;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiRequest<T extends DTOInterface> {
    private T data;
}

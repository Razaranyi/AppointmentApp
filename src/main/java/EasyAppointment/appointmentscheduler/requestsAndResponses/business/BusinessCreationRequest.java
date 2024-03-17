package EasyAppointment.appointmentscheduler.requestsAndResponses.business;

import EasyAppointment.appointmentscheduler.DTO.BusinessDTO;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessCreationRequest {
    @Getter
    private BusinessDTO businessDTO;
}

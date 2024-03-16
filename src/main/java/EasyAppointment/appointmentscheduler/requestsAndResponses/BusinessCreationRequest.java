package EasyAppointment.appointmentscheduler.requestsAndResponses;

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

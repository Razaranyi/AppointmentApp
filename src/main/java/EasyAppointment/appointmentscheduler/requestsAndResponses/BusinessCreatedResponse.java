package EasyAppointment.appointmentscheduler.requestsAndResponses;

import EasyAppointment.appointmentscheduler.models.Business;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessCreatedResponse {

        private String message;
        private Business business;
}

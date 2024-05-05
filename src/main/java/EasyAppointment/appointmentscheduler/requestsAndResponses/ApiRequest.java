package EasyAppointment.appointmentscheduler.requestsAndResponses;

import EasyAppointment.appointmentscheduler.DTO.DTOInterface;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents a generic API request.
 * It contains a data field of type T, which extends DTOInterface.
 * The data field is validated using the @Valid annotation.
 * It uses Lombok annotations for automatic generation of getters, setters, constructors, and builder.
 *
 * @param <T> The type of the data in the request. It must extend DTOInterface.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiRequest<T extends DTOInterface> {

    /**
     * The data contained in the API request.
     * It is validated using the @Valid annotation.
     */
    @Valid
    private T data;
}
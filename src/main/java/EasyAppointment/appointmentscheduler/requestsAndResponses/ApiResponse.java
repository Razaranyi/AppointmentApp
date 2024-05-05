package EasyAppointment.appointmentscheduler.requestsAndResponses;

import lombok.*;

/**
 * This class represents a generic API response.
 * It contains a success field indicating whether the operation was successful,
 * a message field providing additional information about the operation,
 * and a data field of type T.
 * It uses Lombok annotations for automatic generation of getters, setters, constructors, and builder.
 *
 * @param <T> The type of the data in the response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ApiResponse<T>{
    /**
     * Indicates whether the operation was successful.
     */
    private Boolean success;

    /**
     * Provides additional information about the operation.
     */
    private String message;

    /**
     * The data returned in the response.
     */
    private T data;
}
package EasyAppointment.appointmentscheduler.controllers;

import EasyAppointment.appointmentscheduler.DTO.AppointmentDTO;
import EasyAppointment.appointmentscheduler.DTO.BookingDTO;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiRequest;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiResponse;
import EasyAppointment.appointmentscheduler.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * This is the controller for the Booking entity.
 * It handles HTTP requests and responses related to Booking operations.
 */
@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    /**
     * This method handles the POST request to create a new booking.
     * @param request The request body containing the booking details to be created.
     * @return ApiResponse containing BookingDTO
     */
    @PostMapping("/book")
    public ApiResponse<BookingDTO> createNewBooking(@RequestBody ApiRequest<BookingDTO> request) {
        System.out.println(" REQUEST: " + request.toString());
        return bookingService.createNewBooking(request);
    }

    /**
     * This method handles the POST request to cancel a booking.
     * @param appointmentId The ID of the appointment to be cancelled.
     * @return ApiResponse containing BookingDTO
     */
    @PostMapping ("/cancel/{appointmentId}")
    public ApiResponse<BookingDTO> cancelBooking(@PathVariable Long appointmentId) {
        return bookingService.cancelBooking(appointmentId);
    }

    /**
     * This method handles the GET request to retrieve the bookings of the authenticated user within a specific date range.
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return ApiResponse containing List of AppointmentDTO
     */
    @GetMapping("/get-my-bookings")
    public ApiResponse<List<AppointmentDTO>> getMyBookings(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<AppointmentDTO> appointments = bookingService.getMyBookings(start, end);
        return new ApiResponse<>(true, "Bookings fetched successfully", appointments);
    }

}
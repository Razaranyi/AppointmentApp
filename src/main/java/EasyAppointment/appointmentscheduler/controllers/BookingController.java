package EasyAppointment.appointmentscheduler.controllers;

import EasyAppointment.appointmentscheduler.DTO.BookingDTO;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiRequest;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiResponse;
import EasyAppointment.appointmentscheduler.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/book")
    public ApiResponse<BookingDTO> createNewBooking(@RequestBody ApiRequest<BookingDTO> request) {
        System.out.println(" REQUEST: " + request.toString());
        return bookingService.createNewBooking(request);
    }

    @PostMapping ("/cancel/{appointmentId}")
    public ApiResponse<BookingDTO> cancelBooking(@PathVariable Long appointmentId) {
        return bookingService.cancelBooking(appointmentId);
    }
}

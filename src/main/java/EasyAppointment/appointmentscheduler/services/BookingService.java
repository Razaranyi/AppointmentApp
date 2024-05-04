package EasyAppointment.appointmentscheduler.services;

import EasyAppointment.appointmentscheduler.DTO.AppointmentDTO;
import EasyAppointment.appointmentscheduler.DTO.BookingDTO;
import EasyAppointment.appointmentscheduler.DTO.UserDTO;
import EasyAppointment.appointmentscheduler.auth.AuthHelper;
import EasyAppointment.appointmentscheduler.exception.AppointmentAlreadyBookedException;
import EasyAppointment.appointmentscheduler.models.Appointment;
import EasyAppointment.appointmentscheduler.models.Booking;
import EasyAppointment.appointmentscheduler.models.User;
import EasyAppointment.appointmentscheduler.repositories.AppointmentRepository;
import EasyAppointment.appointmentscheduler.repositories.BookingRepository;
import EasyAppointment.appointmentscheduler.repositories.ServiceProviderRepository;
import EasyAppointment.appointmentscheduler.repositories.UserRepository;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiRequest;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final AppointmentRepository appointmentRepository;
    private final BookingRepository bookingRepository;
    private final ServiceProviderRepository serviceProviderRepository;
    private final UserRepository userRepository;

    @Transactional
    public ApiResponse<BookingDTO> createNewBooking(ApiRequest<BookingDTO> request) {
        Set<Appointment> appointments = new HashSet<>(appointmentRepository.findAllById(request.getData().getAppointmentsIds()));

        User user = userRepository.findByEmail(AuthHelper.getCaller())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Building the Booking object with initialized appointments
        Booking booking = Booking.builder()
                .bookingTime(request.getData().getBookingTime())
                .serviceProvider(serviceProviderRepository.getReferenceById(request.getData().getServiceProviderId()))
                .user(user)
                .appointments(new HashSet<>())
                .build();

        if (appointments.isEmpty()) {
            throw new NoSuchElementException("No appointments found with the given ids");
        }

        for (Appointment appointment : appointments) {
            if (!appointment.isAvailable()) {
                throw new AppointmentAlreadyBookedException("One or more appointments are not available");
            }
            appointment.setAvailable(false);
            booking.addAppointment(appointment);
        }

        booking.setStatus("Confirmed");
        booking = bookingRepository.save(booking);
        appointmentRepository.saveAll(appointments); // Saving state changes to appointments

        return new ApiResponse<>(true, "Booking created successfully", new BookingDTO(booking));
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<AppointmentDTO>> getAllUserBookedAppointments(boolean pastBookings) {
        User user = userRepository.findByEmail(AuthHelper.getCaller())
                .orElseThrow(() -> new UsernameNotFoundException("User not found")); //authenticate user so only owned bookings are fetched

        Set<Appointment> appointments = bookingRepository.findByUserId(user.getId()) // Fetch bookings by user's booking IDs
                .stream() // Convert the List to a Stream for further processing
                // or confirmed or Partially Cancelled
                .filter(booking -> "Confirmed".equals(booking.getStatus()) || "Partially Cancelled".equals(booking.getStatus()))
                .flatMap(booking -> booking.getAppointments().stream())
                .collect(Collectors.toSet());

        List<AppointmentDTO> appointmentDTOs = new ArrayList<>(appointments.stream()
                .map(AppointmentDTO::new)
                .toList());
        appointmentDTOs.removeIf(appointmentDto -> pastBookings && appointmentDto.getEndTime().isBefore(LocalDateTime.now()));

        return new ApiResponse<>(true, "Booked appointments fetched successfully", appointmentDTOs);
    }



    @Transactional
    public ApiResponse<BookingDTO> cancelBooking(long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NoSuchElementException("Appointment not found with ID: " + appointmentId));

        Booking booking = appointment.getBooking();

        if ("Confirmed".equals(booking.getStatus())) {
            booking.setStatus("Cancelled");
            for (Appointment appt : booking.getAppointments()) {
                appt.setAvailable(true);
                booking.removeAppointment(appt);
            }
            bookingRepository.save(booking);
            appointmentRepository.saveAll(booking.getAppointments());
            return new ApiResponse<>(true, "Booking cancelled successfully", new BookingDTO(booking));
        } else {
            throw new IllegalStateException("Booking is already cancelled or not in a cancellable state.");
        }
    }


    //this is a function to cancel a booking containing multiple appointments but it is not possible in the ui the book several appointments at once so it's comment out



    //    @Transactional
//    public ApiResponse<BookingDTO> cancelBooked(Set<Long> appointmentId) {
//        Booking booking = bookingRepository.findBookingByAllAppointments(appointmentId, appointmentId.size())
//                .filter(b -> "Confirmed".equals(b.getStatus()))
//                .orElseThrow(() -> new NoSuchElementException("Booking not found"));
//        if (booking.getAppointments().size() == appointmentId.size()) {
//            booking.setStatus("Cancelled");
//        }
//        else booking.setStatus("Partially Cancelled");
//
//        for (Appointment appointment : booking.getAppointments()) {
//            appointment.setAvailable(true);
//            booking.removeAppointment(appointment);
//        }
//        bookingRepository.save(booking);
//        appointmentRepository.saveAll(booking.getAppointments());
//        return new ApiResponse<>(true, "Booking cancelled successfully", new BookingDTO(booking));
//
//
//    }


   @Transactional(readOnly = true)
    public ApiResponse<UserDTO> getBookedUserAppointments(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchElementException("Booking not found"));
        User user = booking.getUser();
        return new ApiResponse<>(true, "User found", new UserDTO(user));
    }
}

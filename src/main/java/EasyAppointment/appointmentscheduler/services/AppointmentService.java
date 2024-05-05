package EasyAppointment.appointmentscheduler.services;


import EasyAppointment.appointmentscheduler.DTO.AppointmentDTO;
import EasyAppointment.appointmentscheduler.models.Appointment;
import EasyAppointment.appointmentscheduler.models.Branch;
import EasyAppointment.appointmentscheduler.models.ServiceProvider;
import EasyAppointment.appointmentscheduler.repositories.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides services related to appointments.
 * It uses Spring's @Service annotation to indicate that it's a service class.
 * It uses Lombok's @RequiredArgsConstructor to automatically generate a constructor with required fields.
 */

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    /**
     * This method retrieves all appointments for a given service provider ID of the week.
     * It is annotated with @Transactional to indicate that it's a transactional method.
     * It is annotated with @Async to indicate that it should be executed asynchronously.
     * It uses the appointmentRepository to retrieve the appointments.
     * It maps the appointments to AppointmentDTO objects using the AppointmentDTO constructor.
     * It collects the mapped AppointmentDTO objects into a list and returns the list.
     *
     * @param serviceProviderId The ID of the service provider.
     * @return A list of AppointmentDTO objects.
     */

    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByServiceProviderIdForWeek(Long serviceProviderId, int weekOffset) {
        LocalDate startOfWeek = LocalDate.now().plusWeeks(weekOffset).with(DayOfWeek.SUNDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        return appointmentRepository.findByServiceProviderIdAndStartTimeBetween(serviceProviderId,
                        startOfWeek.atStartOfDay(),
                        endOfWeek.atTime(LocalTime.MAX))
                .stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * This method retrieves all appointments for a given service provider ID and date.
     * It is annotated with @Transactional to indicate that it's a transactional method.
     * It validates that the date is in the future.
     * It uses the appointmentRepository to retrieve the appointments.
     * It filters the appointments based on the start time being between the start and end of the given date.
     * It sorts the appointments based on the start time.
     * It maps the appointments to AppointmentDTO objects using the AppointmentDTO constructor.
     * It collects the mapped AppointmentDTO objects into a list and returns the list.
     *
     * @param serviceProviderId The ID of the service provider.
     * @param date The date for which appointments are to be retrieved.
     * @return A list of AppointmentDTO objects.
     */

    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByServiceProviderIdForDay(Long serviceProviderId, LocalDate date) {
        //validate future date
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date must be in the future");
        }
        return appointmentRepository.findByServiceProviderIdAndStartTimeBetween(serviceProviderId,
                        date.atStartOfDay(),
                        date.atTime(LocalTime.MAX))
                .stream()
                .sorted(Comparator.comparing(Appointment::getStartTime))
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * This method generates and saves appointments for a given service provider and branch.
     * It is annotated with @Transactional to indicate that it's a transactional method.
     * It is annotated with @Async to indicate that it should be executed asynchronously.
     * It generates appointments for the service provider based on their working days, break hours, and session duration.
     * It uses the appointmentRepository to save the generated appointments.
     *
     * @param serviceProvider The service provider for whom appointments are to be generated.
     * @param branch The branch where the appointments are to be generated.
     */

    @Transactional
    @Async
    public void generateAndSaveServiceProviderSchedule(ServiceProvider serviceProvider, Branch branch) {
        System.out.println("Generating appointments for " + serviceProvider.toString());
        List<Appointment> appointments = new ArrayList<>();
        List<LocalTime[]> breakTimes = parseBreakTimes(serviceProvider.getBreakHour());
        LocalDate today = LocalDate.now();
        LocalDate schedulingHorizon = today.plusMonths(3);  // Adjust the horizon as needed

        boolean[] workingDays = serviceProvider.getWorkingDays();
        int sessionDuration = serviceProvider.getSessionDuration();

        //adding each day until the scheduling horizon and just then moving to the next day
        for (int i = 0; i < workingDays.length; i++) {
            System.out.println("Generating appointments for day " + (i + 1) + "...");

            //skip if not a working day
            if (workingDays[i]) {
                DayOfWeek dayOfWeek = DayOfWeek.of(i + 1);  // Convert integer to DayOfWeek
                LocalDate nextDate = getNextWorkingDay(today, dayOfWeek,workingDays[i]);

                while (!nextDate.isAfter(schedulingHorizon)) { //today is handled as an edge case inside nextDate function
                    LocalDateTime start = LocalDateTime.of(nextDate, branch.getOpeningHours());
                    LocalDateTime end = LocalDateTime.of(nextDate, branch.getClosingHours());

                    while (start.isBefore(end) && start.plusMinutes(sessionDuration).isBefore(end)) { // break on EOD and move to the same day on next week

                        if (!isDuringBreak(start.toLocalTime(), start.plusMinutes(sessionDuration).toLocalTime(), breakTimes)) { //check if the current time is during a break
                            Appointment appointment = Appointment.builder()
                                    .serviceProvider(serviceProvider)
                                    .startTime(start)
                                    .endTime(start.plusMinutes(sessionDuration))
                                    .isAvailable(true)
                                    .duration(sessionDuration)
                                    .build();

                            appointments.add(appointment);
                        }

                        start = start.plusMinutes(sessionDuration); //update current time to the end of the session

                        if (isDuringBreak(start.toLocalTime(), start.plusMinutes(sessionDuration).toLocalTime(), breakTimes)) { //update current time to the end of the break
                            LocalTime nextBreakEnd = findNextBreakEnd(start.toLocalTime(), breakTimes);
                            start = LocalDateTime.of(nextDate, nextBreakEnd);
                        }
                    }

                    // Ensure we get the next working day relative to the just processed day
                    nextDate = getNextWorkingDay(nextDate.plusDays(1), dayOfWeek, false);
                }
            }
        }

        appointmentRepository.saveAll(appointments);
        System.out.println("Generated " + appointments.size() + " appointments for " + serviceProvider.toString());

    }

    /**
     * This method parses the break hours string into a list of LocalTime arrays.
     * It splits the break hours string by commas and iterates over the resulting array.
     * It extracts the start and end times of each break hour and adds them to the list.
     * It returns the list of break times.
     *
     * @param breakTimeString The break hours string to be parsed.
     * @return A list of LocalTime arrays representing the break times.
     */

    private List<LocalTime[]> parseBreakTimes(String breakTimeString) {
        List<LocalTime[]> breakTimes = new ArrayList<>();

        if (breakTimeString == null || breakTimeString.trim().isEmpty() || breakTimeString.equals("[]")) {
            return breakTimes;
        }

        String[] breakTimeArray = breakTimeString.replaceAll("[\\[\\]]", "").split(",");
        for (int i = 0; i < breakTimeArray.length; i += 2) {
            LocalTime start = LocalTime.parse(breakTimeArray[i].trim());
            LocalTime end = LocalTime.parse(breakTimeArray[i + 1].trim());
            breakTimes.add(new LocalTime[]{start, end});
        }

        return breakTimes;
    }

    /**
     * This method checks if the given start and end times are during any of the break times.
     * It iterates over the break times and checks if the start or end time falls within any break interval.
     * It returns true if the start or end time falls within any break interval, false otherwise.
     *
     * @param startTime The start time to be checked.
     * @param endTime The end time to be checked.
     * @param breakTimes The list of break times to check against.
     * @return A boolean value indicating if the given times are during a break.
     */

    private boolean isDuringBreak(LocalTime startTime,LocalTime endTime, List<LocalTime[]> breakTimes) {
        for (LocalTime[] interval : breakTimes) {
            if (!startTime.isBefore(interval[0]) && startTime.isBefore(interval[1])
                    || (endTime.isAfter(interval[0]) && endTime.isBefore(interval[1]))
                    || (startTime.isBefore(interval[0]) && endTime.isAfter(interval[1]))) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method finds the end time of the next break after the given time.
     * It iterates over the break times and checks if the given time falls within any break interval.
     * It returns the end time of the break if the given time falls within any break interval.
     * If the given time is not during any breaks, it returns the given time.
     *
     * @param current The current time to be checked.
     * @param breakTimes The list of break times to check against.
     * @return The end time of the next break after the given time.
     */

    private LocalTime findNextBreakEnd(LocalTime current, List<LocalTime[]> breakTimes) {
        for (LocalTime[] breakTime : breakTimes) {
            if (current.isBefore(breakTime[1]) && !current.isBefore(breakTime[0])) {
                return breakTime[1];
            }
        }
        return current; // If current time is not during any breaks, return current time
    }


    /**
     * This method finds the next working day based on the current date, day of the week, and working days.
     * It checks if the current date is a working day and returns it if it is.
     * If the current date is not a working day, it calculates the number of days to the next working day.
     * It returns the next working day based on the calculated number of days.
     *
     * @param current The current date to start from.
     * @param dayOfWeek The day of the week to find.
     * @param isWorkingDay A boolean array indicating the working days.
     * @return The next working day based on the current date, day of the week, and working days.
     */

    private LocalDate getNextWorkingDay(LocalDate current, DayOfWeek dayOfWeek, boolean isWorkingDay) {
        if (current.getDayOfWeek().equals(dayOfWeek) && isWorkingDay) {
            return current;
        }
        int daysToAdd = (dayOfWeek.getValue() - current.getDayOfWeek().getValue() + 7) % 7;
        return current.plusDays(daysToAdd == 0 ? 7 : daysToAdd);
    }
}

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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

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

    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsByServiceProviderIdForDay(Long serviceProviderId, LocalDate date) {
        //validate future date
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date must be in the future"); //unhandled but the user will get empty list
        }
        return appointmentRepository.findByServiceProviderIdAndStartTimeBetween(serviceProviderId,
                        date.atStartOfDay(),
                        date.atTime(LocalTime.MAX))
                .stream()
                .map(AppointmentDTO::new)
                .collect(Collectors.toList());
    }

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
        for (int i = 0; i < workingDays.length; i++) {
            System.out.println("Generating appointments for day " + (i + 1) + "...");
            if (workingDays[i]) {
                DayOfWeek dayOfWeek = DayOfWeek.of(i + 1);  // Convert integer to DayOfWeek
                LocalDate nextDate = getNextWorkingDay(today, dayOfWeek,workingDays[i]);

                while (!nextDate.isAfter(schedulingHorizon)) {
                    LocalDateTime start = LocalDateTime.of(nextDate, branch.getOpeningHours());
                    LocalDateTime end = LocalDateTime.of(nextDate, branch.getClosingHours());

                    while (start.isBefore(end) && start.plusMinutes(sessionDuration).isBefore(end)) {
                        if (!isDuringBreak(start.toLocalTime(), start.plusMinutes(sessionDuration).toLocalTime(), breakTimes)) {
                            Appointment appointment = Appointment.builder()
                                    .serviceProvider(serviceProvider)
                                    .startTime(start)
                                    .endTime(start.plusMinutes(sessionDuration))
                                    .isAvailable(true)
                                    .duration(sessionDuration)
                                    .build();

                            appointments.add(appointment);
                        }

                        start = start.plusMinutes(sessionDuration);

                        if (isDuringBreak(start.toLocalTime(), start.plusMinutes(sessionDuration).toLocalTime(), breakTimes)) {
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

    private LocalTime findNextBreakEnd(LocalTime current, List<LocalTime[]> breakTimes) {
        for (LocalTime[] breakTime : breakTimes) {
            if (current.isBefore(breakTime[1]) && !current.isBefore(breakTime[0])) {
                return breakTime[1];
            }
        }
        return current; // If current time is not during any breaks, return current time
    }


    private LocalDate getNextWorkingDay(LocalDate current, DayOfWeek dayOfWeek, boolean isWorkingDay) {
        if (current.getDayOfWeek().equals(dayOfWeek) && isWorkingDay) {
            return current;
        }
        int daysToAdd = (dayOfWeek.getValue() - current.getDayOfWeek().getValue() + 7) % 7;
        return current.plusDays(daysToAdd == 0 ? 7 : daysToAdd);
    }
}

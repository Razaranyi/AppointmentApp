package EasyAppointment.appointmentscheduler.services;


import EasyAppointment.appointmentscheduler.DTO.AppointmentDTO;
import EasyAppointment.appointmentscheduler.models.Appointment;
import EasyAppointment.appointmentscheduler.models.Branch;
import EasyAppointment.appointmentscheduler.models.ServiceProvider;
import EasyAppointment.appointmentscheduler.repositories.AppointmentRepository;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public void generateAndSaveServiceProviderSchedule(ServiceProvider serviceProvider, Branch branch) {
        List<Appointment> appointments = new ArrayList<>();
        List<LocalTime[]> breakTimes = parseBreakTimes(serviceProvider.getBreakHour());
        LocalDate today = LocalDate.now();
        LocalDate schedulingHorizon = today.plusMonths(3);  // Adjust the horizon as needed

        int[] workingDaysIntegers = serviceProvider.getWorkingDays();

        for (Integer dayInt : workingDaysIntegers) {
            DayOfWeek dayOfWeek = DayOfWeek.of(dayInt);  // Convert integer to DayOfWeek
            LocalDate nextDate = getNextWorkingDay(today, dayOfWeek);

            while (!nextDate.isAfter(schedulingHorizon)) {
                LocalDateTime start = LocalDateTime.of(nextDate, branch.getOpeningHours());
                LocalDateTime end = LocalDateTime.of(nextDate, branch.getClosingHours());

                while (start.isBefore(end)) {
                    if (!isDuringBreak(start.toLocalTime(), breakTimes)) {
                        Appointment appointment = Appointment.builder()
                                .serviceProvider(serviceProvider)
                                .startTime(start)
                                .endTime(start.plusMinutes(30))  // Assuming 30 minutes for now
                                .isAvailable(true)
                                .duration(30)  // Assuming 30 minutes for now
                                .build();
                        appointments.add(appointment);

                        start = start.plusMinutes(30);  // Increment start time for the next appointment
                    } else {
                        LocalTime nextBreakEnd = findNextBreakEnd(start.toLocalTime(), breakTimes);
                        start = LocalDateTime.of(nextDate, nextBreakEnd);
                    }
                }

                // Ensure we get the next working day relative to the just processed day
                nextDate = getNextWorkingDay(nextDate.plusDays(1), dayOfWeek);
            }
        }

        appointmentRepository.saveAll(appointments);
    }

    private List<LocalTime[]> parseBreakTimes(String breakTimeString) {
        List<LocalTime[]> breakTimes = new ArrayList<>();

        String[] breakTimeArray = breakTimeString.replaceAll("[\\[\\]]", "").split(",");
        for (int i = 0; i < breakTimeArray.length; i += 2) {
            LocalTime start = LocalTime.parse(breakTimeArray[i].trim());
            LocalTime end = LocalTime.parse(breakTimeArray[i + 1].trim());
            breakTimes.add(new LocalTime[]{start, end});
        }

        return breakTimes;
    }

    private boolean isDuringBreak(LocalTime time, List<LocalTime[]> breakTimes) {
        for (LocalTime[] interval : breakTimes) {
            if (!time.isBefore(interval[0]) && time.isBefore(interval[1])) {
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

    private LocalDate getNextWorkingDay(LocalDate current, DayOfWeek dayOfWeek) {
        int daysToAdd = (dayOfWeek.getValue() - current.getDayOfWeek().getValue() + 7) % 7;
        return current.plusDays(daysToAdd == 0 ? 7 : daysToAdd);
    }
}

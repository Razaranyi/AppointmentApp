package EasyAppointment.appointmentscheduler;

import EasyAppointment.appointmentscheduler.repositories.BusinessRepository;
import EasyAppointment.appointmentscheduler.repositories.UserRepository;
import EasyAppointment.appointmentscheduler.models.Business;
import EasyAppointment.appointmentscheduler.models.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class AppointmentSchedulerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppointmentSchedulerBackendApplication.class, args);
	}
//	@Bean
//	CommandLineRunner commandLineRunner(UserRepository userRepository,BusinessRepository businessRepository){
//		return args -> {
//			User user = new User("John Doe", "jhon.doe@gmail.com", "password", false);
//		};
//	}
}



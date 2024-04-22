package EasyAppointment.appointmentscheduler;

import EasyAppointment.appointmentscheduler.services.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@RestController
public class AppointmentSchedulerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppointmentSchedulerBackendApplication.class, args);

	}

    @Bean
    public CategoryInitializer categoryInitializer(CategoryService categoryService) {
        return new CategoryInitializer(categoryService);
    }


}




class CategoryInitializer implements CommandLineRunner {
	private final CategoryService categoryService;

	public CategoryInitializer(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@Override
	public void run(String... args) throws Exception {
		List<String> categories = Arrays.asList(
				"Other",            // None of the below
				"Medical",          // General medical services
				"Pet",              // Pet care services
				"Wellness",           // Health and wellness services
				"Beauty",           // Beauty salons and spas
				"Handyman",         // Home repair and maintenance
				"Dental",           // Dental clinics
				"Massage Therapy",  // Massage services
				"Hair Salon",       // Hairdressing services
				"Nail Salon",       // Nail treatment services
				"Counseling",       // Mental health professionals
				"Tutoring",         // Private educational services
				"Fitness",          // Gyms and personal trainers
				"Yoga Studio",      // Yoga and wellness classes
				"Auto Repair",      // Car mechanic services
				"Electronics Repair", // Electronics and computer repair services
				"Legal Consultation", // Lawyers and legal advisors
				"Accounting",       // Financial and accounting services
				"Real Estate",      // Real estate agencies
				"Photography",      // Professional photography services
				"Event Planning",   // Event organizers and planners
				"Cleaning Services", // Commercial and residential cleaning
				"Tailoring",        // Clothing alterations and tailoring
				"Catering",         // Food catering services
				"Gardening",      	// Gardening and landscaping services
				"Locksmith",        // Emergency locksmith services
				"Veterinary"       // Veterinary clinics for pets

		);
		categoryService.initialCategories(categories);
	}
}




package EasyAppointment.appointmentscheduler.config;

import EasyAppointment.appointmentscheduler.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This is the configuration class for the application.
 * It handles the configuration of authentication and password encoding.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

   private final UserRepository userRepository;

   /**
    * This method provides a UserDetailsService bean.
    * It is used to load user-specific data.
    * @return UserDetailsService
    */
   @Bean
   public UserDetailsService userDetailsService() {
      return username -> userRepository.findByEmail(username)
              .orElseThrow(() -> new UsernameNotFoundException("User with email " + username + " not found"));
   }

   /**
    * This method provides an AuthenticationProvider bean.
    * It is used to authenticate a user in the security context.
    * @return AuthenticationProvider
    */
   @Bean
   public AuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
      authProvider.setUserDetailsService(userDetailsService());
      authProvider.setPasswordEncoder(passwordEncoder());
      return authProvider;
   }

   /**
    * This method provides an AuthenticationManager bean.
    * It is used to manage the authentication within the security context.
    * @param config AuthenticationConfiguration
    * @return AuthenticationManager
    * @throws Exception if unable to get the AuthenticationManager
    */
   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
      return config.getAuthenticationManager();
   }

   /**
    * This method provides a PasswordEncoder bean.
    * It is used to encode the password in the security context.
    * @return PasswordEncoder
    */
   @Bean
   public PasswordEncoder passwordEncoder()  {
      return new BCryptPasswordEncoder();
   }

}
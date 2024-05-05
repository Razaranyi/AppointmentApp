package EasyAppointment.appointmentscheduler.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static EasyAppointment.appointmentscheduler.models.Role.ADMIN;
import static EasyAppointment.appointmentscheduler.models.Role.USER;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * This is the configuration class for the application's security.
 * It handles the configuration of the security filter chain, the authentication provider, and the URL white list.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] WHITE_LIST_URL = { //allowed for all
            "/api/auth/**",
            "/api/user/sign-up",
            "/api/home",};
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    /**
     * This method provides a SecurityFilterChain bean.
     * It configures the HTTP security, including the URL white list, the session management, and the authentication provider.
     * @param http The HttpSecurity.
     * @return The SecurityFilterChain.
     * @throws Exception if an error occurs during the configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests ->
                         requests.requestMatchers(WHITE_LIST_URL)
                        .permitAll()
                        .requestMatchers("/api/businesses/my-business/**").hasRole(ADMIN.name())
                        .requestMatchers("/api/bookings/get-my-bookings").hasAnyRole(USER.name(), ADMIN.name())
                        .requestMatchers("/api/businesses/create").hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers("/api/business/{businessId}/branch/get-all").hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers("/api/businesses/get-id").hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers("/api/categories/all").hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers("/api/user/**").hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers("/api/home/**").hasAnyRole(ADMIN.name(), USER.name())

                        .anyRequest().
                                 authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
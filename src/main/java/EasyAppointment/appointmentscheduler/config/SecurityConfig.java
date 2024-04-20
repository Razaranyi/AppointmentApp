package EasyAppointment.appointmentscheduler.config;

import EasyAppointment.appointmentscheduler.models.Role;
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
//    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests ->
                         requests.requestMatchers(WHITE_LIST_URL)
                        .permitAll()
                        .requestMatchers("/api/businesses/my-business/**").hasRole(ADMIN.name())
                        .requestMatchers("/api/categories/all").hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers(POST,"/api/businesses/create/**").hasAnyRole(USER.name(), ADMIN.name())
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



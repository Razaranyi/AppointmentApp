package EasyAppointment.appointmentscheduler;

import EasyAppointment.appointmentscheduler.DTO.UserDTO;
import EasyAppointment.appointmentscheduler.controllers.AuthController;
import EasyAppointment.appointmentscheduler.exception.UserAlreadyExistException;
import EasyAppointment.appointmentscheduler.models.User;
import EasyAppointment.appointmentscheduler.repositories.UserRepository;
import EasyAppointment.appointmentscheduler.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Specify the order strategy
public class AuthControllerTest {

    @Autowired
    private AuthController authController;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private MockMvc mockMvc;

    @BeforeEach
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }
    @Test
    @Order(1) // Runs first
    @DisplayName("Register user successfully")
    public void testRegisterUserSuccess() throws Exception {
        // Create a valid UserDTO
        UserDTO userDTO = new UserDTO(
                "John Doe",
                "john.doe@example.com",
                "password123",
                false
        );

        // Mock UserService behavior
        UserService mock = Mockito.mock(UserService.class);
        Mockito.when(mock.registerNewUser(userDTO)).thenReturn(new User());

        // Call the registerUser method
        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User registered successfully")));

        // Verify user is saved in the database
        Optional<User> savedUser = userRepository.findByEmail(userDTO.getEmail());
        assertThat(savedUser.isPresent(), is(true));
        assertThat(savedUser.get().getFullName(), is(equalTo(userDTO.getFullName())));
        assertThat(savedUser.get().getEmail(), is(equalTo(userDTO.getEmail())));

        // Clear mocks
        Mockito.reset(mock);
    }

    @Test
    @Order(2)
    @DisplayName("Register user with existing email")
    public void testRegisterUserWithExistingEmail() throws Exception {
        // Create a UserDTO with the same email
        UserDTO userDTO = new UserDTO(
                "roi roi",
                "john.doe@example.com",
                "password123",
                false
        );

        // Mock UserService behavior to throw UserAlreadyExistException
        UserService mock = Mockito.mock(UserService.class);
        Mockito.when(mock.registerNewUser(userDTO)).thenThrow(new UserAlreadyExistException("Email already taken"));

        // Call the registerUser method
        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Email already taken")));

        // Verify user is not saved in the database
        Optional<User> savedUser = userRepository.findByFullName(userDTO.getFullName());
        assertThat(savedUser.isPresent(), is(false));
        Mockito.reset(mock);

    }

    @Test
    @Order(3)
    @DisplayName("Register user with invalid inputs")
    public void testRegisterUserWithInvalidInputs() throws Exception {
        // Create a UserDTO with an invalid inputs
        UserDTO userDTO = new UserDTO(
                "",
                "john.doe",
                "231",
                false
        );

        // Call the registerUser method
        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(allOf(
                        containsString("Full name is required"),
                        containsString("Invalid email format"),
                        containsString("Password must be at least 8 characters")
                )));

        // Verify user is not saved in the database
        Optional<User> savedUser = userRepository.findByFullName(userDTO.getFullName());
        assertThat(savedUser.isPresent(), is(false));
    }

    @Test
    @Order(4)
    @DisplayName("Register user with invalid inputs")
    public void testRegisterUserWithInvalidName() throws Exception {
        // Create a UserDTO with an invalid name
        UserDTO userDTO = new UserDTO(
                "wqezx.",
                "john.doe",
                "231",
                false
        );

        // Call the registerUser method
        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(allOf(
                        containsString("Name must contain only valid characters"),
                        containsString("Invalid email format"),
                        containsString("Password must be at least 8 characters")
                )));

        // Verify user is not saved in the database
        Optional<User> savedUser = userRepository.findByFullName(userDTO.getFullName());
        assertThat(savedUser.isPresent(), is(false));
    }
}


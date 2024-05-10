package EasyAppointment.appointmentscheduler;

import EasyAppointment.appointmentscheduler.auth.AuthController;
import EasyAppointment.appointmentscheduler.requestsAndResponses.authentication.AuthenticationResponse;
import EasyAppointment.appointmentscheduler.auth.AuthenticationService;
import EasyAppointment.appointmentscheduler.requestsAndResponses.authentication.RegisterRequest;
import EasyAppointment.appointmentscheduler.exception.UserAlreadyExistException;
import EasyAppointment.appointmentscheduler.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Specify the order strategy
public class AuthControllerTest {

    @Autowired
    private AuthController authController;

    @MockBean
    private AuthenticationService authService;


    @Mock
    private MockMvc mockMvc;
   @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }
    @Test
    @Order(1)
    @DisplayName("Register user successfully")
    public void testRegisterUserSuccess() throws Exception {
        // Mocking AuthenticationService to return a predefined AuthenticationResponse
        AuthenticationResponse mockResponse = new AuthenticationResponse("dummyToken123");
        Mockito.when(authService.registerUser(Mockito.any(RegisterRequest.class))).thenReturn(mockResponse);

        // Create a valid RegisterRequest object
        RegisterRequest registerRequest = RegisterRequest.builder()
                .fullName("John Doe")
                .email("john.doe@example.com")
                .password("password123")
                .build();

        // Perform the POST request and verify the response
        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("dummyToken123"));
    }


    @Test
    @Order(2)
    @DisplayName("Register user with existing email")
    public void testRegisterUserWithExistingEmail() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .fullName("Jane Doe")
                .email("johl5464n.doe@example.com") // Existing email in the database
                .password("password123")
                .build();

        String expectedResponse = "{\"token\":null,\"message\":\"User with email john.doe@example.com already exists\"}";

        // Mock the behavior of authService to throw UserAlreadyExistException for the existing email
        Mockito.when(authService.registerUser(Mockito.any(RegisterRequest.class)))
                .thenThrow(new UserAlreadyExistException("User with email john.doe@example.com already exists"));

        // Perform the POST request and verify the response matches the expected JSON structure
        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedResponse));
    }




    @Test
    @Order(3)
    @DisplayName("Register user with invalid inputs")
    public void testRegisterUserWithInvalidInputs() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .fullName("")
                .email("invalid-email")
                .password("123")
                .build();

        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(allOf(
                        containsString("Full name is required"),
                        containsString("Invalid email format"),
                        containsString("Password must be at least 8 characters")
                )));
    }

    @Test
    @Order(4)
    @DisplayName("Register user with invalid name's characters")
    public void testRegisterUserWithInvalidName() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .fullName("/.sasd")
                .email("invalid-email")
                .password("123")
                .build();

        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(allOf(
                        containsString("Name must contain only valid characters")
                )));
    }
}


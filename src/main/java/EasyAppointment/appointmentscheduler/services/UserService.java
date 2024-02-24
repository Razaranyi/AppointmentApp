package EasyAppointment.appointmentscheduler.services;

import EasyAppointment.appointmentscheduler.DTO.UserDTO;
import EasyAppointment.appointmentscheduler.exception.UserAlreadyExistException;
import EasyAppointment.appointmentscheduler.models.User;
import EasyAppointment.appointmentscheduler.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public User registerNewUser(UserDTO userDto) throws UserAlreadyExistException {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()){
            throw new UserAlreadyExistException("Email already taken");
        }
        User user = new User(
                userDto.getFullName(),
                userDto.getEmail(),
                passwordEncoder.encode(userDto.getPassword()),
                userDto.isAdmin());
        return userRepository.save(user);
    }


    public User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        // Ensure you hash the password before setting it
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setIsAdmin(userDTO.isAdmin());
        return user;
    }

    public UserDTO convertToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        // Do NOT set the password here for security reasons
        userDTO.setIsAdmin(user.getIsAdmin());
        return userDTO;
    }



}


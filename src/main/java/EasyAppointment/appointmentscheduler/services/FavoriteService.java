package EasyAppointment.appointmentscheduler.services;

import EasyAppointment.appointmentscheduler.models.Favorite;
import EasyAppointment.appointmentscheduler.models.User;
import EasyAppointment.appointmentscheduler.repositories.FavoriteRepository;
import EasyAppointment.appointmentscheduler.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;

    public Favorite addFavorite(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    public List<Favorite> getFavoritesByUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User with id " + id + " not found"));
        List<Favorite> fullList =  favoriteRepository.findByUser(user);
        if (fullList.isEmpty()) {
            return null;
        }
        else if (fullList.size() <= 7) {
            return fullList;
        }
        else {
            Random rand = new Random();
            while (fullList.size() > 7) {
                int index = rand.nextInt(fullList.size());
                fullList.remove(index);
            }
            return fullList;
        }
    }
}

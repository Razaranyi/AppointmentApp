package EasyAppointment.appointmentscheduler.services;

import EasyAppointment.appointmentscheduler.DTO.FavoriteDTO;
import EasyAppointment.appointmentscheduler.auth.AuthHelper;
import EasyAppointment.appointmentscheduler.exception.FavoriteAlreadyExistsException;
import EasyAppointment.appointmentscheduler.models.Business;
import EasyAppointment.appointmentscheduler.models.Favorite;
import EasyAppointment.appointmentscheduler.models.User;
import EasyAppointment.appointmentscheduler.repositories.BusinessRepository;
import EasyAppointment.appointmentscheduler.repositories.FavoriteRepository;
import EasyAppointment.appointmentscheduler.repositories.UserRepository;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiRequest;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;

   @Transactional
    public ApiResponse<List<FavoriteDTO>> getFavoritesForAuthenticatedUser() {
        String authenticatedUserEmail = AuthHelper.getCaller();

        User user = userRepository.findByEmail(authenticatedUserEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + authenticatedUserEmail));

        List<Favorite> fullList = favoriteRepository.findByUser(user);
        List<FavoriteDTO> favoriteDTOs;

        if (fullList.isEmpty()) {
            favoriteDTOs = Collections.emptyList();
        } else if (fullList.size() <= 7) {
            favoriteDTOs = fullList.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } else {
            favoriteDTOs = new Random().ints(7, 0, fullList.size())
                    .mapToObj(fullList::get)
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }

        return new ApiResponse<>(true, "Favorites fetched successfully", favoriteDTOs);
    }


    @Transactional
    public ApiResponse<FavoriteDTO> addFavorite(long id, String userEmail) throws RuntimeException {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Business not found with id: " + id)
);
        if (favoriteRepository.existsByUserEmailAndBusinessId(userEmail, id)) {
            favoriteRepository.delete(favoriteRepository.findByUserEmailAndBusinessId(userEmail, id));
            return new ApiResponse<>(true, "Favorite removed", null);
        }
        else  {
            Favorite newFavorite = Favorite.builder()
                    .user(user)
                    .business(business)
                    .build();
            Favorite savedFavorite = favoriteRepository.save(newFavorite);
            user.setFavorite(savedFavorite);
            userRepository.save(user);
            FavoriteDTO favoriteDTO = new FavoriteDTO(savedFavorite.getId(), savedFavorite.getUser().getId(), savedFavorite.getBusiness().getId());
            return new ApiResponse<>(true, "Favorite added", favoriteDTO);
        }


    }
    private FavoriteDTO convertToDTO(Favorite favorite) {
        return new FavoriteDTO(favorite.getId(), favorite.getUser().getId(), favorite.getBusiness().getId());
    }

}

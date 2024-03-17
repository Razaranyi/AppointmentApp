package EasyAppointment.appointmentscheduler.services;

import EasyAppointment.appointmentscheduler.DTO.FavoriteDTO;
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

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;

    public ApiResponse<List<FavoriteDTO>> getFavoritesForAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUserEmail = authentication.getName(); // Get the email from the current authentication principal

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

    private FavoriteDTO convertToDTO(Favorite favorite) {
        // Assume FavoriteDTO has a constructor that takes a Favorite object
        // or set properties individually based on your FavoriteDTO's structure
        return new FavoriteDTO(favorite.getId(), favorite.getUser().getId(), favorite.getBusiness().getId());
    }


    public ApiResponse<FavoriteDTO> addFavorite(ApiRequest<FavoriteDTO> request, String userEmail) throws RuntimeException {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));
        Business business = businessRepository.findById(request.getData().getBusinessId())
                .orElseThrow(() -> new RuntimeException("Business not found with id: " + request.getData().getBusinessId())
);
        if (favoriteRepository.existsByUserEmailAndBusinessId(userEmail, request.getData().getBusinessId())) {
            throw new RuntimeException("Favorite already exists");
        }

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

package EasyAppointment.appointmentscheduler.controllers;

import EasyAppointment.appointmentscheduler.models.Favorite;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import EasyAppointment.appointmentscheduler.services.FavoriteService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @GetMapping("/my-favorites")
    public ResponseEntity<List<Favorite>> getMyFavorites() {
        List<Favorite> favorites = favoriteService.getFavoritesForAuthenticatedUser();
        return ResponseEntity.ok(favorites);
    }

}

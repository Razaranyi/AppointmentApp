package EasyAppointment.appointmentscheduler.controllers;

import EasyAppointment.appointmentscheduler.DTO.FavoriteDTO;
import EasyAppointment.appointmentscheduler.auth.AuthHelper;
import EasyAppointment.appointmentscheduler.models.Favorite;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiRequest;
import EasyAppointment.appointmentscheduler.requestsAndResponses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import EasyAppointment.appointmentscheduler.services.FavoriteService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @GetMapping("/my-favorites") // get list of up to 7 personal favorites for the authenticated user
    public ResponseEntity<ApiResponse<List<FavoriteDTO>>> getMyFavorites() {
            return ResponseEntity.ok(favoriteService.getFavoritesForAuthenticatedUser());
    }


    @PostMapping("/add-or-remove/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<FavoriteDTO>> addFavorite( // add business to the favorites
            @PathVariable Long id){
        String userEmail = AuthHelper.getCaller();

            return ResponseEntity.ok(favoriteService.addFavorite(id, userEmail));
    }


}

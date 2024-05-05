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

/**
 * This is the controller for the Favorite entity.
 * It handles HTTP requests and responses related to Favorite operations.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    /**
     * This method handles the GET request to retrieve the list of up to 7 personal favorites for the authenticated user.
     *
     * @return ResponseEntity containing ApiResponse with List of FavoriteDTO
     */
    @GetMapping("/my-favorites")
    public ResponseEntity<ApiResponse<List<FavoriteDTO>>> getMyFavorites() {
        return ResponseEntity.ok(favoriteService.getFavoritesForAuthenticatedUser());
    }

    /**
     * This method handles the POST request to add a business to the favorites of the authenticated user.
     * @param id The ID of the business to be added to favorites.
     * @return ResponseEntity containing ApiResponse with FavoriteDTO
     */
    @PostMapping("/add-or-remove/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<FavoriteDTO>> addFavorite(
            @PathVariable Long id){
        String userEmail = AuthHelper.getCaller();
        return ResponseEntity.ok(favoriteService.addFavorite(id, userEmail));
    }
}
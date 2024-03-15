package EasyAppointment.appointmentscheduler.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import EasyAppointment.appointmentscheduler.services.FavoriteService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @GetMapping("user/{userId}")
    public ResponseEntity<?> getFavoritesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(favoriteService.getFavoritesByUser(userId));
    }

}

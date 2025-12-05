package com.cardvault.controller;

import com.cardvault.dto.AchievementResponse;
import com.cardvault.dto.UserAchievementResponse;
import com.cardvault.model.Achievement;
import com.cardvault.model.UserAchievement;
import com.cardvault.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/achievements")
@CrossOrigin(origins = "http://localhost:3000")
public class AchievementController {

    private final AchievementService achievementService;

    @Autowired
    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @GetMapping
    public ResponseEntity<List<AchievementResponse>> getAllAchievements() {
        List<Achievement> achievements = achievementService.getAllAchievements();
        List<AchievementResponse> response = achievements.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AchievementResponse> getAchievementById(@PathVariable Long id) {
        return achievementService.getAchievementById(id)
                .map(achievement -> ResponseEntity.ok(convertToResponse(achievement)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserAchievementResponse>> getUserAchievements(@PathVariable UUID userId) {
        List<UserAchievement> userAchievements = achievementService.getUserAchievementsSorted(userId);
        List<UserAchievementResponse> response = userAchievements.stream()
                .map(this::convertToUserAchievementResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> getUserAchievementCount(@PathVariable UUID userId) {
        Long count = achievementService.getUserAchievementCount(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/user/{userId}/points")
    public ResponseEntity<Long> getUserTotalPoints(@PathVariable UUID userId) {
        Long points = achievementService.getUserTotalPoints(userId);
        return ResponseEntity.ok(points);
    }

    @PostMapping("/user/{userId}/award/{achievementId}")
    public ResponseEntity<UserAchievementResponse> awardAchievement(@PathVariable UUID userId,
                                                                    @PathVariable Long achievementId) {
        UserAchievement userAchievement = achievementService.awardAchievement(userId, achievementId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertToUserAchievementResponse(userAchievement));
    }

    @GetMapping("/user/{userId}/has/{achievementId}")
    public ResponseEntity<Boolean> hasAchievement(@PathVariable UUID userId,
                                                  @PathVariable Long achievementId) {
        boolean hasAchievement = achievementService.hasAchievement(userId, achievementId);
        return ResponseEntity.ok(hasAchievement);
    }

    private AchievementResponse convertToResponse(Achievement achievement) {
        AchievementResponse response = new AchievementResponse();
        response.setId(achievement.getId());
        response.setName(achievement.getName());
        response.setDescription(achievement.getDescription());
        response.setIcon(achievement.getIcon());
        response.setCriteria(achievement.getCriteria());
        response.setPoints(achievement.getPoints());
        response.setCreatedAt(achievement.getCreatedAt());
        return response;
    }

    private UserAchievementResponse convertToUserAchievementResponse(UserAchievement userAchievement) {
        UserAchievementResponse response = new UserAchievementResponse();
        response.setId(userAchievement.getId());
        response.setUserId(userAchievement.getUser().getId());
        response.setAchievement(convertToResponse(userAchievement.getAchievement()));
        response.setEarnedAt(userAchievement.getEarnedAt());
        return response;
    }
}

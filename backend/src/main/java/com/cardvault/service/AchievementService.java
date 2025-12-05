package com.cardvault.service;

import com.cardvault.model.Achievement;
import com.cardvault.model.User;
import com.cardvault.model.UserAchievement;
import com.cardvault.repository.AchievementRepository;
import com.cardvault.repository.UserAchievementRepository;
import com.cardvault.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final UserRepository userRepository;

    @Autowired
    public AchievementService(AchievementRepository achievementRepository,
                             UserAchievementRepository userAchievementRepository,
                             UserRepository userRepository) {
        this.achievementRepository = achievementRepository;
        this.userAchievementRepository = userAchievementRepository;
        this.userRepository = userRepository;
    }

    public List<Achievement> getAllAchievements() {
        return achievementRepository.findAll();
    }

    public Optional<Achievement> getAchievementById(Long id) {
        return achievementRepository.findById(id);
    }

    public Optional<Achievement> getAchievementByName(String name) {
        return achievementRepository.findByName(name);
    }

    public Achievement createAchievement(Achievement achievement) {
        if (achievementRepository.existsByName(achievement.getName())) {
            throw new RuntimeException("Achievement already exists with name: " + achievement.getName());
        }
        return achievementRepository.save(achievement);
    }

    public List<UserAchievement> getUserAchievements(UUID userId) {
        return userAchievementRepository.findByUserId(userId);
    }

    public List<UserAchievement> getUserAchievementsSorted(UUID userId) {
        return userAchievementRepository.findByUserIdOrderByEarnedAtDesc(userId);
    }

    public UserAchievement awardAchievement(UUID userId, Long achievementId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new RuntimeException("Achievement not found with id: " + achievementId));

        if (userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId)) {
            throw new RuntimeException("User already has this achievement");
        }

        UserAchievement userAchievement = new UserAchievement();
        userAchievement.setUser(user);
        userAchievement.setAchievement(achievement);

        return userAchievementRepository.save(userAchievement);
    }

    public boolean hasAchievement(UUID userId, Long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    public Long getUserAchievementCount(UUID userId) {
        return userAchievementRepository.countByUserId(userId);
    }

    public Long getUserTotalPoints(UUID userId) {
        Long points = userAchievementRepository.sumPointsByUserId(userId);
        return points != null ? points : 0L;
    }

    public void checkAndAwardAchievements(UUID userId, Long totalCards, BigDecimal collectionValue) {
        if (totalCards >= 1 && !hasAchievement(userId, 1L)) {
            awardAchievementByName(userId, "First Card");
        }
        if (totalCards >= 50 && !hasAchievement(userId, 2L)) {
            awardAchievementByName(userId, "Collector");
        }
        if (totalCards >= 100 && !hasAchievement(userId, 3L)) {
            awardAchievementByName(userId, "Master Collector");
        }
        if (collectionValue.compareTo(new BigDecimal("1000")) >= 0 && !hasAchievement(userId, 6L)) {
            awardAchievementByName(userId, "High Roller");
        }
    }

    private void awardAchievementByName(UUID userId, String achievementName) {
        Optional<Achievement> achievement = achievementRepository.findByName(achievementName);
        if (achievement.isPresent()) {
            try {
                awardAchievement(userId, achievement.get().getId());
            } catch (RuntimeException e) {
            }
        }
    }
}

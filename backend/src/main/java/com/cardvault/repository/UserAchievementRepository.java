package com.cardvault.repository;

import com.cardvault.model.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserAchievementRepository extends JpaRepository<UserAchievement, UUID> {

    List<UserAchievement> findByUserId(UUID userId);

    List<UserAchievement> findByUserIdOrderByEarnedAtDesc(UUID userId);

    boolean existsByUserIdAndAchievementId(UUID userId, Long achievementId);

    @Query("SELECT COUNT(ua) FROM UserAchievement ua WHERE ua.user.id = :userId")
    Long countByUserId(@Param("userId") UUID userId);

    @Query("SELECT SUM(a.points) FROM UserAchievement ua JOIN ua.achievement a WHERE ua.user.id = :userId")
    Long sumPointsByUserId(@Param("userId") UUID userId);
}

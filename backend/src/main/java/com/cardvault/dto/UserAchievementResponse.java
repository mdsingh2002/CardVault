package com.cardvault.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAchievementResponse {

    private UUID id;
    private UUID userId;
    private AchievementResponse achievement;
    private LocalDateTime earnedAt;
}

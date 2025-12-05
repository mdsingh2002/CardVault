package com.cardvault.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AchievementResponse {

    private Long id;
    private String name;
    private String description;
    private String icon;
    private String criteria;
    private Integer points;
    private LocalDateTime createdAt;
}

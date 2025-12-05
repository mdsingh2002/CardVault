package com.cardvault.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistResponse {

    private UUID id;
    private UUID userId;
    private CardResponse card;
    private Integer priority;
    private BigDecimal maxPrice;
    private String notes;
    private LocalDateTime createdAt;
}

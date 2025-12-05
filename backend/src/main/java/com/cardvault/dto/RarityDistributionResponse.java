package com.cardvault.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RarityDistributionResponse {

    private String rarity;
    private Long cardCount;
    private Long totalQuantity;
    private BigDecimal totalValue;
}

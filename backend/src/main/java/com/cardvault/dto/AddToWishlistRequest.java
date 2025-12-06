package com.cardvault.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddToWishlistRequest {
    private String cardApiId;
    private Integer priority = 1;
    private BigDecimal maxPrice;
    private String notes;
}

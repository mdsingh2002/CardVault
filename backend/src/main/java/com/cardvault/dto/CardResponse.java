package com.cardvault.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardResponse {

    private UUID id;
    private String apiId;
    private String name;
    private String setName;
    private String setSeries;
    private String cardNumber;
    private String rarity;
    private String cardType;
    private String supertype;
    private String subtypes;
    private Integer hp;
    private String artist;
    private String imageUrl;
    private String smallImageUrl;
    private BigDecimal marketPrice;
    private LocalDate releaseDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

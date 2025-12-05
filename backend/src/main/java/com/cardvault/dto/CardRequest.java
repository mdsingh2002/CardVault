package com.cardvault.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardRequest {

    private String apiId;

    @NotBlank(message = "Card name is required")
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
}

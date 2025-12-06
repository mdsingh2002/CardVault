package com.cardvault.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonSetDto {
    private String id;
    private String name;
    private String series;
    private Integer printedTotal;
    private Integer total;
    private PokemonCardDto.Legalities legalities;
    private String ptcgoCode;
    private String releaseDate;
    private String updatedAt;
    private PokemonCardDto.CardImages images;
}

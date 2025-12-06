package com.cardvault.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonCardResponse {
    private List<PokemonCardDto> data;
    private int page;
    private int pageSize;
    private int count;
    private int totalCount;
}

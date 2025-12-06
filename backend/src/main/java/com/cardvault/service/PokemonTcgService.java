package com.cardvault.service;

import com.cardvault.dto.PokemonCardDto;
import com.cardvault.dto.PokemonCardResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class PokemonTcgService {

    private static final Logger logger = LoggerFactory.getLogger(PokemonTcgService.class);
    private static final String BASE_URL = "https://api.pokemontcg.io/v2";

    @Value("${pokemon.tcg.api.key:}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public PokemonTcgService() {
        this.restTemplate = new RestTemplate();
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (apiKey != null && !apiKey.isEmpty()) {
            headers.set("X-Api-Key", apiKey);
        }
        return headers;
    }

    public PokemonCardResponse searchCards(String query, int page, int pageSize) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/cards")
                    .queryParam("page", page)
                    .queryParam("pageSize", pageSize);

            if (query != null && !query.isEmpty()) {
                builder.queryParam("q", query);
            }

            String url = builder.toUriString();
            logger.info("Making request to Pokemon TCG API: {}", url);

            HttpEntity<String> entity = new HttpEntity<>(getHeaders());
            ResponseEntity<PokemonCardResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    PokemonCardResponse.class
            );

            logger.info("Successfully fetched cards from Pokemon TCG API");
            return response.getBody();
        } catch (Exception e) {
            logger.error("Error fetching cards from Pokemon TCG API: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch cards from Pokemon TCG API: " + e.getMessage(), e);
        }
    }

    public PokemonCardDto getCardById(String cardId) {
        try {
            String url = BASE_URL + "/cards/" + cardId;
            HttpEntity<String> entity = new HttpEntity<>(getHeaders());

            ResponseEntity<SingleCardResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    SingleCardResponse.class
            );

            logger.info("Successfully fetched card {} from Pokemon TCG API", cardId);
            return response.getBody() != null ? response.getBody().getData() : null;
        } catch (Exception e) {
            logger.error("Error fetching card {} from Pokemon TCG API: {}", cardId, e.getMessage());
            throw new RuntimeException("Failed to fetch card from Pokemon TCG API", e);
        }
    }

    private static class SingleCardResponse {
        private PokemonCardDto data;

        public SingleCardResponse() {
        }

        public PokemonCardDto getData() {
            return data;
        }

        public void setData(PokemonCardDto data) {
            this.data = data;
        }
    }

    public PokemonCardResponse searchCardsByName(String name, int page, int pageSize) {
        // Pokemon TCG API uses case-insensitive partial matching
        // Format: name:cardname (no wildcards needed for partial match)
        String query = "name:" + name;
        logger.info("Searching for cards with query: {}", query);
        return searchCards(query, page, pageSize);
    }

    public PokemonCardResponse getCardsBySet(String setId, int page, int pageSize) {
        String query = "set.id:" + setId;
        return searchCards(query, page, pageSize);
    }

    public PokemonCardResponse getCardsByType(String type, int page, int pageSize) {
        String query = "types:" + type;
        return searchCards(query, page, pageSize);
    }

    public PokemonCardResponse getCardsByRarity(String rarity, int page, int pageSize) {
        String query = "rarity:\"" + rarity + "\"";
        return searchCards(query, page, pageSize);
    }
}

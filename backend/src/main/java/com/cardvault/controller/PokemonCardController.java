package com.cardvault.controller;

import com.cardvault.dto.PokemonCardDto;
import com.cardvault.dto.PokemonCardResponse;
import com.cardvault.service.PokemonTcgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pokemon")
@CrossOrigin(origins = "http://localhost:3000")
public class PokemonCardController {

    @Autowired
    private PokemonTcgService pokemonTcgService;

    @GetMapping("/cards")
    public ResponseEntity<PokemonCardResponse> searchCards(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        PokemonCardResponse response = pokemonTcgService.searchCards(q, page, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cards/{id}")
    public ResponseEntity<PokemonCardDto> getCardById(@PathVariable String id) {
        PokemonCardDto card = pokemonTcgService.getCardById(id);
        if (card != null) {
            return ResponseEntity.ok(card);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/cards/search/name")
    public ResponseEntity<PokemonCardResponse> searchByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        PokemonCardResponse response = pokemonTcgService.searchCardsByName(name, page, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cards/set/{setId}")
    public ResponseEntity<PokemonCardResponse> getCardsBySet(
            @PathVariable String setId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        PokemonCardResponse response = pokemonTcgService.getCardsBySet(setId, page, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cards/type/{type}")
    public ResponseEntity<PokemonCardResponse> getCardsByType(
            @PathVariable String type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        PokemonCardResponse response = pokemonTcgService.getCardsByType(type, page, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cards/rarity/{rarity}")
    public ResponseEntity<PokemonCardResponse> getCardsByRarity(
            @PathVariable String rarity,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        PokemonCardResponse response = pokemonTcgService.getCardsByRarity(rarity, page, pageSize);
        return ResponseEntity.ok(response);
    }
}

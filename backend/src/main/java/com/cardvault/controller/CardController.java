package com.cardvault.controller;

import com.cardvault.dto.CardRequest;
import com.cardvault.dto.CardResponse;
import com.cardvault.model.Card;
import com.cardvault.service.CardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cards")
@CrossOrigin(origins = "http://localhost:3000")
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public ResponseEntity<List<CardResponse>> getAllCards() {
        List<Card> cards = cardService.getAllCards();
        List<CardResponse> response = cards.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardResponse> getCardById(@PathVariable UUID id) {
        return cardService.getCardById(id)
                .map(card -> ResponseEntity.ok(convertToResponse(card)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/api-id/{apiId}")
    public ResponseEntity<CardResponse> getCardByApiId(@PathVariable String apiId) {
        return cardService.getCardByApiId(apiId)
                .map(card -> ResponseEntity.ok(convertToResponse(card)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<CardResponse>> searchCards(@RequestParam String query) {
        List<Card> cards = cardService.searchCards(query);
        List<CardResponse> response = cards.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/set/{setName}")
    public ResponseEntity<List<CardResponse>> getCardsBySet(@PathVariable String setName) {
        List<Card> cards = cardService.getCardsBySetOrdered(setName);
        List<CardResponse> response = cards.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rarity/{rarity}")
    public ResponseEntity<List<CardResponse>> getCardsByRarity(@PathVariable String rarity) {
        List<Card> cards = cardService.getCardsByRarity(rarity);
        List<CardResponse> response = cards.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CardResponse> createCard(@Valid @RequestBody CardRequest request) {
        Card card = convertToEntity(request);
        Card savedCard = cardService.createCard(card);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponse(savedCard));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardResponse> updateCard(@PathVariable UUID id, @Valid @RequestBody CardRequest request) {
        Card card = convertToEntity(request);
        Card updatedCard = cardService.updateCard(id, card);
        return ResponseEntity.ok(convertToResponse(updatedCard));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable UUID id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }

    private CardResponse convertToResponse(Card card) {
        CardResponse response = new CardResponse();
        response.setId(card.getId());
        response.setApiId(card.getApiId());
        response.setName(card.getName());
        response.setSetName(card.getSetName());
        response.setSetSeries(card.getSetSeries());
        response.setCardNumber(card.getCardNumber());
        response.setRarity(card.getRarity());
        response.setCardType(card.getCardType());
        response.setSupertype(card.getSupertype());
        response.setSubtypes(card.getSubtypes());
        response.setHp(card.getHp());
        response.setArtist(card.getArtist());
        response.setImageUrl(card.getImageUrl());
        response.setSmallImageUrl(card.getSmallImageUrl());
        response.setMarketPrice(card.getMarketPrice());
        response.setReleaseDate(card.getReleaseDate());
        response.setCreatedAt(card.getCreatedAt());
        response.setUpdatedAt(card.getUpdatedAt());
        return response;
    }

    private Card convertToEntity(CardRequest request) {
        Card card = new Card();
        card.setApiId(request.getApiId());
        card.setName(request.getName());
        card.setSetName(request.getSetName());
        card.setSetSeries(request.getSetSeries());
        card.setCardNumber(request.getCardNumber());
        card.setRarity(request.getRarity());
        card.setCardType(request.getCardType());
        card.setSupertype(request.getSupertype());
        card.setSubtypes(request.getSubtypes());
        card.setHp(request.getHp());
        card.setArtist(request.getArtist());
        card.setImageUrl(request.getImageUrl());
        card.setSmallImageUrl(request.getSmallImageUrl());
        card.setMarketPrice(request.getMarketPrice());
        card.setReleaseDate(request.getReleaseDate());
        return card;
    }
}

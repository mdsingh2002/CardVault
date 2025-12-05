package com.cardvault.controller;

import com.cardvault.dto.CardResponse;
import com.cardvault.dto.CollectionSummaryResponse;
import com.cardvault.dto.UserCardRequest;
import com.cardvault.dto.UserCardResponse;
import com.cardvault.model.UserCard;
import com.cardvault.service.AchievementService;
import com.cardvault.service.UserCardService;
import com.cardvault.service.WishlistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user-cards")
@CrossOrigin(origins = "http://localhost:3000")
public class UserCardController {

    private final UserCardService userCardService;
    private final AchievementService achievementService;
    private final WishlistService wishlistService;

    @Autowired
    public UserCardController(UserCardService userCardService,
                             AchievementService achievementService,
                             WishlistService wishlistService) {
        this.userCardService = userCardService;
        this.achievementService = achievementService;
        this.wishlistService = wishlistService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserCardResponse>> getUserCollection(@PathVariable UUID userId) {
        List<UserCard> userCards = userCardService.getUserCollectionSorted(userId);
        List<UserCardResponse> response = userCards.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserCardResponse> getUserCardById(@PathVariable UUID id) {
        return userCardService.getUserCardById(id)
                .map(userCard -> ResponseEntity.ok(convertToResponse(userCard)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}/summary")
    public ResponseEntity<CollectionSummaryResponse> getCollectionSummary(@PathVariable UUID userId) {
        CollectionSummaryResponse summary = new CollectionSummaryResponse();
        summary.setUniqueCards(userCardService.getCollectionCount(userId));
        summary.setTotalCards(userCardService.getTotalCardQuantity(userId));
        summary.setTotalValue(userCardService.getTotalCollectionValue(userId));
        summary.setAchievementCount(achievementService.getUserAchievementCount(userId));
        summary.setTotalPoints(achievementService.getUserTotalPoints(userId));
        summary.setWishlistCount(wishlistService.getWishlistCount(userId));
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/user/{userId}/top-cards")
    public ResponseEntity<List<UserCardResponse>> getTopValueCards(@PathVariable UUID userId,
                                                                   @RequestParam(defaultValue = "10") int limit) {
        List<UserCard> topCards = userCardService.getTopValueCards(userId);
        List<UserCardResponse> response = topCards.stream()
                .limit(limit)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/rarity/{rarity}")
    public ResponseEntity<List<UserCardResponse>> getCardsByRarity(@PathVariable UUID userId,
                                                                   @PathVariable String rarity) {
        List<UserCard> userCards = userCardService.getCardsByRarity(userId, rarity);
        List<UserCardResponse> response = userCards.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/set/{setName}")
    public ResponseEntity<List<UserCardResponse>> getCardsBySet(@PathVariable UUID userId,
                                                                @PathVariable String setName) {
        List<UserCard> userCards = userCardService.getCardsBySet(userId, setName);
        List<UserCardResponse> response = userCards.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<UserCardResponse> addCardToCollection(@PathVariable UUID userId,
                                                                @Valid @RequestBody UserCardRequest request) {
        UserCard userCard = userCardService.addCardToCollection(
                userId,
                request.getCardId(),
                request.getQuantity(),
                request.getConditionId(),
                request.getPurchasePrice(),
                request.getCurrentValue()
        );

        Long totalCards = userCardService.getTotalCardQuantity(userId);
        BigDecimal totalValue = userCardService.getTotalCollectionValue(userId);
        achievementService.checkAndAwardAchievements(userId, totalCards, totalValue);

        return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponse(userCard));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserCardResponse> updateUserCard(@PathVariable UUID id,
                                                           @Valid @RequestBody UserCardRequest request) {
        UserCard userCard = new UserCard();
        userCard.setQuantity(request.getQuantity());
        userCard.setPurchasePrice(request.getPurchasePrice());
        userCard.setCurrentValue(request.getCurrentValue());
        userCard.setAcquisitionDate(request.getAcquisitionDate());
        userCard.setNotes(request.getNotes());
        userCard.setIsGraded(request.getIsGraded());
        userCard.setGradeValue(request.getGradeValue());
        userCard.setGradingCompany(request.getGradingCompany());

        UserCard updatedUserCard = userCardService.updateUserCard(id, userCard);
        return ResponseEntity.ok(convertToResponse(updatedUserCard));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCardFromCollection(@PathVariable UUID id) {
        userCardService.removeCardFromCollection(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/quantity")
    public ResponseEntity<UserCardResponse> updateQuantity(@PathVariable UUID id,
                                                           @RequestParam Integer quantity) {
        UserCard updatedUserCard = userCardService.updateCardQuantity(id, quantity);
        return ResponseEntity.ok(convertToResponse(updatedUserCard));
    }

    @PatchMapping("/{id}/value")
    public ResponseEntity<UserCardResponse> updateValue(@PathVariable UUID id,
                                                        @RequestParam BigDecimal value) {
        UserCard updatedUserCard = userCardService.updateCardValue(id, value);
        return ResponseEntity.ok(convertToResponse(updatedUserCard));
    }

    private UserCardResponse convertToResponse(UserCard userCard) {
        UserCardResponse response = new UserCardResponse();
        response.setId(userCard.getId());
        response.setUserId(userCard.getUser().getId());
        response.setCard(convertCardToResponse(userCard.getCard()));
        response.setQuantity(userCard.getQuantity());
        response.setCondition(userCard.getCondition() != null ? userCard.getCondition().getName() : null);
        response.setPurchasePrice(userCard.getPurchasePrice());
        response.setCurrentValue(userCard.getCurrentValue());

        BigDecimal totalValue = userCard.getCurrentValue() != null ?
                userCard.getCurrentValue().multiply(BigDecimal.valueOf(userCard.getQuantity())) :
                BigDecimal.ZERO;
        response.setTotalValue(totalValue);

        response.setAcquisitionDate(userCard.getAcquisitionDate());
        response.setNotes(userCard.getNotes());
        response.setIsGraded(userCard.getIsGraded());
        response.setGradeValue(userCard.getGradeValue());
        response.setGradingCompany(userCard.getGradingCompany());
        response.setCreatedAt(userCard.getCreatedAt());
        response.setUpdatedAt(userCard.getUpdatedAt());
        return response;
    }

    private CardResponse convertCardToResponse(com.cardvault.model.Card card) {
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
}

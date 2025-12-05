package com.cardvault.controller;

import com.cardvault.dto.CardResponse;
import com.cardvault.dto.WishlistRequest;
import com.cardvault.dto.WishlistResponse;
import com.cardvault.model.Wishlist;
import com.cardvault.service.WishlistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "http://localhost:3000")
public class WishlistController {

    private final WishlistService wishlistService;

    @Autowired
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WishlistResponse>> getUserWishlist(@PathVariable UUID userId) {
        List<Wishlist> wishlist = wishlistService.getUserWishlistByPriority(userId);
        List<WishlistResponse> response = wishlist.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WishlistResponse> getWishlistItemById(@PathVariable UUID id) {
        return wishlistService.getWishlistItemById(id)
                .map(wishlist -> ResponseEntity.ok(convertToResponse(wishlist)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}/high-priority")
    public ResponseEntity<List<WishlistResponse>> getHighPriorityItems(@PathVariable UUID userId,
                                                                       @RequestParam(defaultValue = "3") Integer minPriority) {
        List<Wishlist> wishlist = wishlistService.getHighPriorityItems(userId, minPriority);
        List<WishlistResponse> response = wishlist.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> getWishlistCount(@PathVariable UUID userId) {
        Long count = wishlistService.getWishlistCount(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/user/{userId}/has/{cardId}")
    public ResponseEntity<Boolean> isInWishlist(@PathVariable UUID userId, @PathVariable UUID cardId) {
        boolean inWishlist = wishlistService.isInWishlist(userId, cardId);
        return ResponseEntity.ok(inWishlist);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<WishlistResponse> addToWishlist(@PathVariable UUID userId,
                                                          @Valid @RequestBody WishlistRequest request) {
        Wishlist wishlist = wishlistService.addToWishlist(
                userId,
                request.getCardId(),
                request.getPriority(),
                request.getMaxPrice(),
                request.getNotes()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponse(wishlist));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WishlistResponse> updateWishlistItem(@PathVariable UUID id,
                                                               @Valid @RequestBody WishlistRequest request) {
        Wishlist wishlist = new Wishlist();
        wishlist.setPriority(request.getPriority());
        wishlist.setMaxPrice(request.getMaxPrice());
        wishlist.setNotes(request.getNotes());

        Wishlist updatedWishlist = wishlistService.updateWishlistItem(id, wishlist);
        return ResponseEntity.ok(convertToResponse(updatedWishlist));
    }

    @PatchMapping("/{id}/priority")
    public ResponseEntity<WishlistResponse> updatePriority(@PathVariable UUID id,
                                                           @RequestParam Integer priority) {
        Wishlist updatedWishlist = wishlistService.updatePriority(id, priority);
        return ResponseEntity.ok(convertToResponse(updatedWishlist));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFromWishlist(@PathVariable UUID id) {
        wishlistService.removeFromWishlist(id);
        return ResponseEntity.noContent().build();
    }

    private WishlistResponse convertToResponse(Wishlist wishlist) {
        WishlistResponse response = new WishlistResponse();
        response.setId(wishlist.getId());
        response.setUserId(wishlist.getUser().getId());
        response.setCard(convertCardToResponse(wishlist.getCard()));
        response.setPriority(wishlist.getPriority());
        response.setMaxPrice(wishlist.getMaxPrice());
        response.setNotes(wishlist.getNotes());
        response.setCreatedAt(wishlist.getCreatedAt());
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

package com.cardvault.controller;

import com.cardvault.dto.AddToWishlistRequest;
import com.cardvault.dto.CardResponse;
import com.cardvault.dto.WishlistRequest;
import com.cardvault.dto.WishlistResponse;
import com.cardvault.model.Wishlist;
import com.cardvault.repository.UserRepository;
import com.cardvault.service.WishlistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "http://localhost:3000")
public class WishlistController {

    private final WishlistService wishlistService;
    private final UserRepository userRepository;

    @Autowired
    public WishlistController(WishlistService wishlistService, UserRepository userRepository) {
        this.wishlistService = wishlistService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<WishlistResponse>> getUserWishlist() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = getUserIdFromAuth(auth);
        List<Wishlist> wishlist = wishlistService.getUserWishlistByPriority(userId);
        List<WishlistResponse> response = wishlist.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WishlistResponse> getWishlistItemById(@PathVariable UUID id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = getUserIdFromAuth(auth);
        return wishlistService.getWishlistItemById(id)
                .filter(wishlist -> wishlist.getUser().getId().equals(userId))
                .map(wishlist -> ResponseEntity.ok(convertToResponse(wishlist)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/high-priority")
    public ResponseEntity<List<WishlistResponse>> getHighPriorityItems(@RequestParam(defaultValue = "3") Integer minPriority) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = getUserIdFromAuth(auth);
        List<Wishlist> wishlist = wishlistService.getHighPriorityItems(userId, minPriority);
        List<WishlistResponse> response = wishlist.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getWishlistCount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = getUserIdFromAuth(auth);
        Long count = wishlistService.getWishlistCount(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/has/{cardId}")
    public ResponseEntity<Boolean> isInWishlist(@PathVariable UUID cardId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = getUserIdFromAuth(auth);
        boolean inWishlist = wishlistService.isInWishlist(userId, cardId);
        return ResponseEntity.ok(inWishlist);
    }

    @PostMapping
    public ResponseEntity<WishlistResponse> addToWishlist(@Valid @RequestBody AddToWishlistRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = getUserIdFromAuth(auth);
        Wishlist wishlist = wishlistService.addToWishlistByApiId(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponse(wishlist));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WishlistResponse> updateWishlistItem(@PathVariable UUID id,
                                                               @Valid @RequestBody WishlistRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = getUserIdFromAuth(auth);
        Wishlist wishlist = new Wishlist();
        wishlist.setPriority(request.getPriority());
        wishlist.setMaxPrice(request.getMaxPrice());
        wishlist.setNotes(request.getNotes());

        Wishlist updatedWishlist = wishlistService.updateWishlistItem(id, wishlist);
        if (!updatedWishlist.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(convertToResponse(updatedWishlist));
    }

    @PatchMapping("/{id}/priority")
    public ResponseEntity<WishlistResponse> updatePriority(@PathVariable UUID id,
                                                           @RequestParam Integer priority) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = getUserIdFromAuth(auth);
        Wishlist updatedWishlist = wishlistService.updatePriority(id, priority);
        if (!updatedWishlist.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(convertToResponse(updatedWishlist));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFromWishlist(@PathVariable UUID id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = getUserIdFromAuth(auth);
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

    private UUID getUserIdFromAuth(Authentication auth) {
        String username = auth.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username))
                .getId();
    }
}

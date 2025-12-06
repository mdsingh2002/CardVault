package com.cardvault.service;

import com.cardvault.dto.AddToWishlistRequest;
import com.cardvault.dto.PokemonCardDto;
import com.cardvault.model.Card;
import com.cardvault.model.User;
import com.cardvault.model.Wishlist;
import com.cardvault.repository.CardRepository;
import com.cardvault.repository.UserRepository;
import com.cardvault.repository.WishlistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class WishlistService {

    private static final Logger logger = LoggerFactory.getLogger(WishlistService.class);

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    @Autowired
    private PokemonTcgService pokemonTcgService;

    @Autowired
    public WishlistService(WishlistRepository wishlistRepository,
                          UserRepository userRepository,
                          CardRepository cardRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
    }

    public List<Wishlist> getUserWishlist(UUID userId) {
        return wishlistRepository.findByUserId(userId);
    }

    public List<Wishlist> getUserWishlistByPriority(UUID userId) {
        return wishlistRepository.findByUserIdOrderByPriorityDesc(userId);
    }

    public Optional<Wishlist> getWishlistItemById(UUID id) {
        return wishlistRepository.findById(id);
    }

    public Wishlist addToWishlist(UUID userId, UUID cardId, Integer priority, BigDecimal maxPrice, String notes) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + cardId));

        if (wishlistRepository.existsByUserIdAndCardId(userId, cardId)) {
            throw new RuntimeException("Card already in wishlist");
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setCard(card);
        wishlist.setPriority(priority != null ? priority : 1);
        wishlist.setMaxPrice(maxPrice);
        wishlist.setNotes(notes);

        return wishlistRepository.save(wishlist);
    }

    public Wishlist updateWishlistItem(UUID id, Wishlist wishlistDetails) {
        Wishlist wishlist = wishlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wishlist item not found with id: " + id));

        if (wishlistDetails.getPriority() != null) {
            if (wishlistDetails.getPriority() < 1 || wishlistDetails.getPriority() > 5) {
                throw new RuntimeException("Priority must be between 1 and 5");
            }
            wishlist.setPriority(wishlistDetails.getPriority());
        }
        if (wishlistDetails.getMaxPrice() != null) {
            wishlist.setMaxPrice(wishlistDetails.getMaxPrice());
        }
        if (wishlistDetails.getNotes() != null) {
            wishlist.setNotes(wishlistDetails.getNotes());
        }

        return wishlistRepository.save(wishlist);
    }

    public void removeFromWishlist(UUID id) {
        if (!wishlistRepository.existsById(id)) {
            throw new RuntimeException("Wishlist item not found with id: " + id);
        }
        wishlistRepository.deleteById(id);
    }

    public boolean isInWishlist(UUID userId, UUID cardId) {
        return wishlistRepository.existsByUserIdAndCardId(userId, cardId);
    }

    public Long getWishlistCount(UUID userId) {
        return wishlistRepository.countByUserId(userId);
    }

    public List<Wishlist> getHighPriorityItems(UUID userId, Integer minPriority) {
        return wishlistRepository.findHighPriorityItems(userId, minPriority != null ? minPriority : 3);
    }

    public Wishlist updatePriority(UUID wishlistItemId, Integer newPriority) {
        if (newPriority < 1 || newPriority > 5) {
            throw new RuntimeException("Priority must be between 1 and 5");
        }

        Wishlist wishlist = wishlistRepository.findById(wishlistItemId)
                .orElseThrow(() -> new RuntimeException("Wishlist item not found with id: " + wishlistItemId));

        wishlist.setPriority(newPriority);
        return wishlistRepository.save(wishlist);
    }
}

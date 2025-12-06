package com.cardvault.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "wishlist_cards")
public class WishlistCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "card_id", nullable = false)
    private String cardId;

    @Column(name = "card_name", nullable = false)
    private String cardName;

    @Column(name = "card_set")
    private String cardSet;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "card_rarity")
    private String cardRarity;

    @Column(name = "card_image_url")
    private String cardImageUrl;

    @Column(name = "priority")
    private Integer priority = 1;

    @Column(name = "max_price")
    private Double maxPrice;

    @Column(name = "current_price")
    private Double currentPrice;

    @Column(name = "notes", length = 1000)
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

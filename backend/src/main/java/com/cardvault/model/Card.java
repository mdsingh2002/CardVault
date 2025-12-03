package com.cardvault.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "api_id", unique = true, length = 100)
    private String apiId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "set_name", length = 100)
    private String setName;

    @Column(name = "set_series", length = 100)
    private String setSeries;

    @Column(name = "card_number", length = 50)
    private String cardNumber;

    @Column(length = 50)
    private String rarity;

    @Column(name = "card_type", length = 50)
    private String cardType;

    @Column(length = 50)
    private String supertype;

    @Column(columnDefinition = "TEXT")
    private String subtypes;

    private Integer hp;

    @Column(length = 100)
    private String artist;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "small_image_url", columnDefinition = "TEXT")
    private String smallImageUrl;

    @Column(name = "market_price", precision = 10, scale = 2)
    private BigDecimal marketPrice;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

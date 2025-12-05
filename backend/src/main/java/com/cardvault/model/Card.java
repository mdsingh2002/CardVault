package com.cardvault.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cards")
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

    public Card() {
    }

    public Card(UUID id, String apiId, String name, String setName, String setSeries, String cardNumber,
                String rarity, String cardType, String supertype, String subtypes, Integer hp, String artist,
                String imageUrl, String smallImageUrl, BigDecimal marketPrice, LocalDate releaseDate,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.apiId = apiId;
        this.name = name;
        this.setName = setName;
        this.setSeries = setSeries;
        this.cardNumber = cardNumber;
        this.rarity = rarity;
        this.cardType = cardType;
        this.supertype = supertype;
        this.subtypes = subtypes;
        this.hp = hp;
        this.artist = artist;
        this.imageUrl = imageUrl;
        this.smallImageUrl = smallImageUrl;
        this.marketPrice = marketPrice;
        this.releaseDate = releaseDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public String getSetSeries() {
        return setSeries;
    }

    public void setSetSeries(String setSeries) {
        this.setSeries = setSeries;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getSupertype() {
        return supertype;
    }

    public void setSupertype(String supertype) {
        this.supertype = supertype;
    }

    public String getSubtypes() {
        return subtypes;
    }

    public void setSubtypes(String subtypes) {
        this.subtypes = subtypes;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    public void setSmallImageUrl(String smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

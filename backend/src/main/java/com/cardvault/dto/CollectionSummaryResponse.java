package com.cardvault.dto;

import java.math.BigDecimal;

public class CollectionSummaryResponse {

    private Long uniqueCards;
    private Long totalCards;
    private BigDecimal totalValue;
    private Long achievementCount;
    private Long totalPoints;
    private Long wishlistCount;

    public CollectionSummaryResponse() {
    }

    public CollectionSummaryResponse(Long uniqueCards, Long totalCards, BigDecimal totalValue,
                                    Long achievementCount, Long totalPoints, Long wishlistCount) {
        this.uniqueCards = uniqueCards;
        this.totalCards = totalCards;
        this.totalValue = totalValue;
        this.achievementCount = achievementCount;
        this.totalPoints = totalPoints;
        this.wishlistCount = wishlistCount;
    }

    public Long getUniqueCards() {
        return uniqueCards;
    }

    public void setUniqueCards(Long uniqueCards) {
        this.uniqueCards = uniqueCards;
    }

    public Long getTotalCards() {
        return totalCards;
    }

    public void setTotalCards(Long totalCards) {
        this.totalCards = totalCards;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public Long getAchievementCount() {
        return achievementCount;
    }

    public void setAchievementCount(Long achievementCount) {
        this.achievementCount = achievementCount;
    }

    public Long getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Long totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Long getWishlistCount() {
        return wishlistCount;
    }

    public void setWishlistCount(Long wishlistCount) {
        this.wishlistCount = wishlistCount;
    }
}

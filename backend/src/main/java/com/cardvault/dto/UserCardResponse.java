package com.cardvault.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class UserCardResponse {

    private UUID id;
    private UUID userId;
    private CardResponse card;
    private Integer quantity;
    private String condition;
    private BigDecimal purchasePrice;
    private BigDecimal currentValue;
    private BigDecimal totalValue;
    private LocalDate acquisitionDate;
    private String notes;
    private Boolean isGraded;
    private String gradeValue;
    private String gradingCompany;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserCardResponse() {
    }

    public UserCardResponse(UUID id, UUID userId, CardResponse card, Integer quantity, String condition,
                           BigDecimal purchasePrice, BigDecimal currentValue, BigDecimal totalValue,
                           LocalDate acquisitionDate, String notes, Boolean isGraded, String gradeValue,
                           String gradingCompany, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.card = card;
        this.quantity = quantity;
        this.condition = condition;
        this.purchasePrice = purchasePrice;
        this.currentValue = currentValue;
        this.totalValue = totalValue;
        this.acquisitionDate = acquisitionDate;
        this.notes = notes;
        this.isGraded = isGraded;
        this.gradeValue = gradeValue;
        this.gradingCompany = gradingCompany;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public CardResponse getCard() {
        return card;
    }

    public void setCard(CardResponse card) {
        this.card = card;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsGraded() {
        return isGraded;
    }

    public void setIsGraded(Boolean isGraded) {
        this.isGraded = isGraded;
    }

    public String getGradeValue() {
        return gradeValue;
    }

    public void setGradeValue(String gradeValue) {
        this.gradeValue = gradeValue;
    }

    public String getGradingCompany() {
        return gradingCompany;
    }

    public void setGradingCompany(String gradingCompany) {
        this.gradingCompany = gradingCompany;
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

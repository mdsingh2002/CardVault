package com.cardvault.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class UserCardRequest {

    @NotNull(message = "Card ID is required")
    private UUID cardId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private Long conditionId;
    private BigDecimal purchasePrice;
    private BigDecimal currentValue;
    private LocalDate acquisitionDate;
    private String notes;
    private Boolean isGraded;
    private String gradeValue;
    private String gradingCompany;

    public UserCardRequest() {
    }

    public UserCardRequest(UUID cardId, Integer quantity, Long conditionId, BigDecimal purchasePrice,
                          BigDecimal currentValue, LocalDate acquisitionDate, String notes,
                          Boolean isGraded, String gradeValue, String gradingCompany) {
        this.cardId = cardId;
        this.quantity = quantity;
        this.conditionId = conditionId;
        this.purchasePrice = purchasePrice;
        this.currentValue = currentValue;
        this.acquisitionDate = acquisitionDate;
        this.notes = notes;
        this.isGraded = isGraded;
        this.gradeValue = gradeValue;
        this.gradingCompany = gradingCompany;
    }

    public UUID getCardId() {
        return cardId;
    }

    public void setCardId(UUID cardId) {
        this.cardId = cardId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getConditionId() {
        return conditionId;
    }

    public void setConditionId(Long conditionId) {
        this.conditionId = conditionId;
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
}

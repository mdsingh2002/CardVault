package com.cardvault.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddToCollectionRequest {
    private String cardApiId;
    private Integer quantity = 1;
    private String conditionId;
    private BigDecimal purchasePrice;
    private String notes;
    private Boolean isGraded = false;
    private String gradeValue;
    private String gradingCompany;
}

package com.cardvault.dto;

import com.cardvault.model.UserCard;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserCardDto {
    private UUID id;
    private UUID userId;
    private CardDto card;
    private Integer quantity;
    private String condition;
    private BigDecimal purchasePrice;
    private BigDecimal currentValue;
    private LocalDate acquisitionDate;
    private String notes;
    private Boolean isGraded;
    private String gradeValue;
    private String gradingCompany;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    public static class CardDto {
        private UUID id;
        private String apiId;
        private String name;
        private String setName;
        private String cardNumber;
        private String rarity;
        private String imageUrl;
        private String smallImageUrl;
        private BigDecimal marketPrice;
    }

    public static UserCardDto fromEntity(UserCard userCard) {
        UserCardDto dto = new UserCardDto();
        dto.setId(userCard.getId());
        dto.setUserId(userCard.getUser().getId());
        dto.setQuantity(userCard.getQuantity());
        dto.setPurchasePrice(userCard.getPurchasePrice());
        dto.setCurrentValue(userCard.getCurrentValue());
        dto.setAcquisitionDate(userCard.getAcquisitionDate());
        dto.setNotes(userCard.getNotes());
        dto.setIsGraded(userCard.getIsGraded());
        dto.setGradeValue(userCard.getGradeValue());
        dto.setGradingCompany(userCard.getGradingCompany());
        dto.setCreatedAt(userCard.getCreatedAt());
        dto.setUpdatedAt(userCard.getUpdatedAt());

        if (userCard.getCondition() != null) {
            dto.setCondition(userCard.getCondition().getName());
        }

        CardDto cardDto = new CardDto();
        cardDto.setId(userCard.getCard().getId());
        cardDto.setApiId(userCard.getCard().getApiId());
        cardDto.setName(userCard.getCard().getName());
        cardDto.setSetName(userCard.getCard().getSetName());
        cardDto.setCardNumber(userCard.getCard().getCardNumber());
        cardDto.setRarity(userCard.getCard().getRarity());
        cardDto.setImageUrl(userCard.getCard().getImageUrl());
        cardDto.setSmallImageUrl(userCard.getCard().getSmallImageUrl());
        cardDto.setMarketPrice(userCard.getCard().getMarketPrice());
        dto.setCard(cardDto);

        return dto;
    }
}

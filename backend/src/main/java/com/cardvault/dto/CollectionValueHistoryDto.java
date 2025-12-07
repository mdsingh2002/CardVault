package com.cardvault.dto;

import com.cardvault.model.CollectionValueHistory;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CollectionValueHistoryDto {
    private BigDecimal totalValue;
    private Integer totalCards;
    private Integer uniqueCards;
    private LocalDateTime recordedAt;

    public static CollectionValueHistoryDto fromEntity(CollectionValueHistory entity) {
        CollectionValueHistoryDto dto = new CollectionValueHistoryDto();
        dto.setTotalValue(entity.getTotalValue());
        dto.setTotalCards(entity.getTotalCards());
        dto.setUniqueCards(entity.getUniqueCards());
        dto.setRecordedAt(entity.getRecordedAt());
        return dto;
    }
}

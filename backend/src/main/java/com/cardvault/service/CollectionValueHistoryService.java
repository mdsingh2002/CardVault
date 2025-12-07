package com.cardvault.service;

import com.cardvault.model.CollectionValueHistory;
import com.cardvault.model.User;
import com.cardvault.model.UserCard;
import com.cardvault.repository.CollectionValueHistoryRepository;
import com.cardvault.repository.UserCardRepository;
import com.cardvault.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CollectionValueHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(CollectionValueHistoryService.class);

    @Autowired
    private CollectionValueHistoryRepository historyRepository;

    @Autowired
    private UserCardRepository userCardRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Record a snapshot of the user's collection value
     */
    @Transactional
    public CollectionValueHistory recordSnapshot(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<UserCard> collection = userCardRepository.findByUserIdOrderByCreatedAtDesc(userId);

        BigDecimal totalValue = BigDecimal.ZERO;
        int totalCards = 0;
        int uniqueCards = collection.size();

        for (UserCard userCard : collection) {
            BigDecimal cardValue = userCard.getCurrentValue() != null
                ? userCard.getCurrentValue()
                : (userCard.getCard().getMarketPrice() != null ? userCard.getCard().getMarketPrice() : BigDecimal.ZERO);

            totalValue = totalValue.add(cardValue.multiply(BigDecimal.valueOf(userCard.getQuantity())));
            totalCards += userCard.getQuantity();
        }

        CollectionValueHistory snapshot = new CollectionValueHistory(user, totalValue, totalCards, uniqueCards);
        CollectionValueHistory saved = historyRepository.save(snapshot);

        logger.info("Recorded collection value snapshot for user {}: ${}", userId, totalValue);
        return saved;
    }

    /**
     * Get all history for a user
     */
    public List<CollectionValueHistory> getHistory(UUID userId) {
        return historyRepository.findByUserIdOrderByRecordedAtAsc(userId);
    }

    /**
     * Get history since a specific date
     */
    public List<CollectionValueHistory> getHistorySince(UUID userId, LocalDateTime since) {
        return historyRepository.findByUserIdSince(userId, since);
    }

    /**
     * Get history for the last N days
     */
    public List<CollectionValueHistory> getHistoryForLastDays(UUID userId, int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return historyRepository.findByUserIdSince(userId, since);
    }
}

package com.cardvault.service;

import com.cardvault.model.Card;
import com.cardvault.model.CardCondition;
import com.cardvault.model.User;
import com.cardvault.model.UserCard;
import com.cardvault.repository.CardRepository;
import com.cardvault.repository.CardConditionRepository;
import com.cardvault.repository.UserCardRepository;
import com.cardvault.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserCardService {

    private final UserCardRepository userCardRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final CardConditionRepository cardConditionRepository;

    @Autowired
    public UserCardService(UserCardRepository userCardRepository,
                          UserRepository userRepository,
                          CardRepository cardRepository,
                          CardConditionRepository cardConditionRepository) {
        this.userCardRepository = userCardRepository;
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.cardConditionRepository = cardConditionRepository;
    }

    public List<UserCard> getUserCollection(UUID userId) {
        return userCardRepository.findByUserId(userId);
    }

    public List<UserCard> getUserCollectionSorted(UUID userId) {
        return userCardRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Optional<UserCard> getUserCardById(UUID id) {
        return userCardRepository.findById(id);
    }

    public UserCard addCardToCollection(UUID userId, UUID cardId, Integer quantity,
                                       Long conditionId, BigDecimal purchasePrice,
                                       BigDecimal currentValue) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + cardId));

        CardCondition condition = null;
        if (conditionId != null) {
            condition = cardConditionRepository.findById(conditionId)
                    .orElseThrow(() -> new RuntimeException("Condition not found with id: " + conditionId));
        }

        UserCard userCard = new UserCard();
        userCard.setUser(user);
        userCard.setCard(card);
        userCard.setQuantity(quantity != null ? quantity : 1);
        userCard.setCondition(condition);
        userCard.setPurchasePrice(purchasePrice);
        userCard.setCurrentValue(currentValue != null ? currentValue : card.getMarketPrice());

        return userCardRepository.save(userCard);
    }

    public UserCard updateUserCard(UUID id, UserCard userCardDetails) {
        UserCard userCard = userCardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User card not found with id: " + id));

        if (userCardDetails.getQuantity() != null) {
            userCard.setQuantity(userCardDetails.getQuantity());
        }
        if (userCardDetails.getCondition() != null) {
            userCard.setCondition(userCardDetails.getCondition());
        }
        if (userCardDetails.getPurchasePrice() != null) {
            userCard.setPurchasePrice(userCardDetails.getPurchasePrice());
        }
        if (userCardDetails.getCurrentValue() != null) {
            userCard.setCurrentValue(userCardDetails.getCurrentValue());
        }
        if (userCardDetails.getAcquisitionDate() != null) {
            userCard.setAcquisitionDate(userCardDetails.getAcquisitionDate());
        }
        if (userCardDetails.getNotes() != null) {
            userCard.setNotes(userCardDetails.getNotes());
        }
        if (userCardDetails.getIsGraded() != null) {
            userCard.setIsGraded(userCardDetails.getIsGraded());
        }
        if (userCardDetails.getGradeValue() != null) {
            userCard.setGradeValue(userCardDetails.getGradeValue());
        }
        if (userCardDetails.getGradingCompany() != null) {
            userCard.setGradingCompany(userCardDetails.getGradingCompany());
        }

        return userCardRepository.save(userCard);
    }

    public void removeCardFromCollection(UUID id) {
        if (!userCardRepository.existsById(id)) {
            throw new RuntimeException("User card not found with id: " + id);
        }
        userCardRepository.deleteById(id);
    }

    public Long getCollectionCount(UUID userId) {
        return userCardRepository.countByUserId(userId);
    }

    public Long getTotalCardQuantity(UUID userId) {
        Long total = userCardRepository.sumQuantityByUserId(userId);
        return total != null ? total : 0L;
    }

    public BigDecimal getTotalCollectionValue(UUID userId) {
        BigDecimal total = userCardRepository.sumTotalValueByUserId(userId);
        return total != null ? total : BigDecimal.ZERO;
    }

    public List<UserCard> getTopValueCards(UUID userId) {
        return userCardRepository.findTopValueCardsByUserId(userId);
    }

    public List<UserCard> getCardsByRarity(UUID userId, String rarity) {
        return userCardRepository.findByUserIdAndRarity(userId, rarity);
    }

    public List<UserCard> getCardsBySet(UUID userId, String setName) {
        return userCardRepository.findByUserIdAndSetName(userId, setName);
    }

    public UserCard updateCardQuantity(UUID userCardId, Integer newQuantity) {
        if (newQuantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        UserCard userCard = userCardRepository.findById(userCardId)
                .orElseThrow(() -> new RuntimeException("User card not found with id: " + userCardId));

        userCard.setQuantity(newQuantity);
        return userCardRepository.save(userCard);
    }

    public UserCard updateCardValue(UUID userCardId, BigDecimal newValue) {
        UserCard userCard = userCardRepository.findById(userCardId)
                .orElseThrow(() -> new RuntimeException("User card not found with id: " + userCardId));

        userCard.setCurrentValue(newValue);
        return userCardRepository.save(userCard);
    }
}

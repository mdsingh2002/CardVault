package com.cardvault.service;

import com.cardvault.model.Card;
import com.cardvault.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CardService {

    private final CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    public Optional<Card> getCardById(UUID id) {
        return cardRepository.findById(id);
    }

    public Optional<Card> getCardByApiId(String apiId) {
        return cardRepository.findByApiId(apiId);
    }

    public Card createCard(Card card) {
        return cardRepository.save(card);
    }

    public Card updateCard(UUID id, Card cardDetails) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + id));

        card.setName(cardDetails.getName());
        card.setSetName(cardDetails.getSetName());
        card.setSetSeries(cardDetails.getSetSeries());
        card.setCardNumber(cardDetails.getCardNumber());
        card.setRarity(cardDetails.getRarity());
        card.setCardType(cardDetails.getCardType());
        card.setSupertype(cardDetails.getSupertype());
        card.setSubtypes(cardDetails.getSubtypes());
        card.setHp(cardDetails.getHp());
        card.setArtist(cardDetails.getArtist());
        card.setImageUrl(cardDetails.getImageUrl());
        card.setSmallImageUrl(cardDetails.getSmallImageUrl());
        card.setMarketPrice(cardDetails.getMarketPrice());
        card.setReleaseDate(cardDetails.getReleaseDate());

        return cardRepository.save(card);
    }

    public void deleteCard(UUID id) {
        if (!cardRepository.existsById(id)) {
            throw new RuntimeException("Card not found with id: " + id);
        }
        cardRepository.deleteById(id);
    }

    public List<Card> searchCardsByName(String name) {
        return cardRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Card> getCardsBySetName(String setName) {
        return cardRepository.findBySetName(setName);
    }

    public List<Card> getCardsByRarity(String rarity) {
        return cardRepository.findByRarity(rarity);
    }

    public List<Card> getCardsByType(String type) {
        return cardRepository.findByCardType(type);
    }

    public List<Card> searchCards(String searchTerm) {
        return cardRepository.searchCards(searchTerm);
    }

    public List<Card> getCardsBySetOrdered(String setName) {
        return cardRepository.findBySetNameOrderByCardNumberAsc(setName);
    }

    public Card createOrUpdateCard(Card card) {
        if (card.getApiId() != null) {
            Optional<Card> existingCard = cardRepository.findByApiId(card.getApiId());
            if (existingCard.isPresent()) {
                Card existing = existingCard.get();
                existing.setMarketPrice(card.getMarketPrice());
                existing.setImageUrl(card.getImageUrl());
                existing.setSmallImageUrl(card.getSmallImageUrl());
                return cardRepository.save(existing);
            }
        }
        return cardRepository.save(card);
    }
}

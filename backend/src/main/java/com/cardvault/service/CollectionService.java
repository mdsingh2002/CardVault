package com.cardvault.service;

import com.cardvault.dto.AddToCollectionRequest;
import com.cardvault.dto.PokemonCardDto;
import com.cardvault.model.Card;
import com.cardvault.model.CardCondition;
import com.cardvault.model.User;
import com.cardvault.model.UserCard;
import com.cardvault.repository.CardConditionRepository;
import com.cardvault.repository.CardRepository;
import com.cardvault.repository.UserCardRepository;
import com.cardvault.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CollectionService {

    private static final Logger logger = LoggerFactory.getLogger(CollectionService.class);

    @Autowired
    private UserCardRepository userCardRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardConditionRepository cardConditionRepository;

    @Autowired
    private PokemonTcgService pokemonTcgService;

    @Transactional
    public UserCard addToCollection(UUID userId, AddToCollectionRequest request) {
        logger.info("Adding card {} to collection for user {}", request.getCardApiId(), userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Card card = cardRepository.findByApiId(request.getCardApiId())
                .orElseGet(() -> {
                    PokemonCardDto pokemonCard = pokemonTcgService.getCardById(request.getCardApiId());
                    return createCardFromPokemonDto(pokemonCard);
                });

        Optional<UserCard> existingUserCard = userCardRepository.findByUserIdAndCardId(userId, card.getId());

        if (existingUserCard.isPresent()) {
            UserCard userCard = existingUserCard.get();
            userCard.setQuantity(userCard.getQuantity() + request.getQuantity());
            logger.info("Updated quantity for existing card in collection");
            return userCardRepository.save(userCard);
        }

        UserCard userCard = new UserCard();
        userCard.setUser(user);
        userCard.setCard(card);
        userCard.setQuantity(request.getQuantity());
        userCard.setPurchasePrice(request.getPurchasePrice());
        userCard.setCurrentValue(card.getMarketPrice());
        userCard.setAcquisitionDate(LocalDate.now());
        userCard.setNotes(request.getNotes());
        userCard.setIsGraded(request.getIsGraded());
        userCard.setGradeValue(request.getGradeValue());
        userCard.setGradingCompany(request.getGradingCompany());

        if (request.getConditionId() != null) {
            CardCondition condition = cardConditionRepository.findById(Long.parseLong(request.getConditionId()))
                    .orElse(null);
            userCard.setCondition(condition);
        }

        logger.info("Created new card in collection");
        return userCardRepository.save(userCard);
    }

    public List<UserCard> getUserCollection(UUID userId) {
        return userCardRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Optional<UserCard> getUserCardById(UUID userId, UUID userCardId) {
        return userCardRepository.findById(userCardId)
                .filter(uc -> uc.getUser().getId().equals(userId));
    }

    @Transactional
    public UserCard updateUserCard(UUID userId, UUID userCardId, AddToCollectionRequest request) {
        UserCard userCard = userCardRepository.findById(userCardId)
                .filter(uc -> uc.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("User card not found"));

        if (request.getQuantity() != null) {
            userCard.setQuantity(request.getQuantity());
        }
        if (request.getPurchasePrice() != null) {
            userCard.setPurchasePrice(request.getPurchasePrice());
        }
        if (request.getNotes() != null) {
            userCard.setNotes(request.getNotes());
        }
        if (request.getIsGraded() != null) {
            userCard.setIsGraded(request.getIsGraded());
        }
        if (request.getGradeValue() != null) {
            userCard.setGradeValue(request.getGradeValue());
        }
        if (request.getGradingCompany() != null) {
            userCard.setGradingCompany(request.getGradingCompany());
        }
        if (request.getConditionId() != null) {
            CardCondition condition = cardConditionRepository.findById(Long.parseLong(request.getConditionId()))
                    .orElse(null);
            userCard.setCondition(condition);
        }

        return userCardRepository.save(userCard);
    }

    @Transactional
    public void removeFromCollection(UUID userId, UUID userCardId) {
        UserCard userCard = userCardRepository.findById(userCardId)
                .filter(uc -> uc.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("User card not found"));

        userCardRepository.delete(userCard);
        logger.info("Removed card {} from collection for user {}", userCardId, userId);
    }

    private Card createCardFromPokemonDto(PokemonCardDto dto) {
        Card card = new Card();
        card.setApiId(dto.getId());
        card.setName(dto.getName());
        card.setSetName(dto.getSet() != null ? dto.getSet().getName() : null);
        card.setSetSeries(dto.getSet() != null ? dto.getSet().getSeries() : null);
        card.setCardNumber(dto.getNumber());
        card.setRarity(dto.getRarity());
        card.setSupertype(dto.getSupertype());
        card.setSubtypes(dto.getSubtypes() != null ? String.join(",", dto.getSubtypes()) : null);
        if (dto.getHp() != null) {
            try {
                card.setHp(Integer.parseInt(dto.getHp()));
            } catch (NumberFormatException e) {
                logger.warn("Failed to parse HP value: {}", dto.getHp());
                card.setHp(null);
            }
        }
        card.setArtist(dto.getArtist());
        card.setImageUrl(dto.getImages() != null ? dto.getImages().getLarge() : null);
        card.setSmallImageUrl(dto.getImages() != null ? dto.getImages().getSmall() : null);

        if (dto.getTcgplayer() != null && dto.getTcgplayer().getPrices() != null) {
            BigDecimal marketPrice = extractMarketPrice(dto.getTcgplayer().getPrices());
            card.setMarketPrice(marketPrice);
        }

        if (dto.getSet() != null && dto.getSet().getReleaseDate() != null) {
            try {
                card.setReleaseDate(LocalDate.parse(dto.getSet().getReleaseDate()));
            } catch (Exception e) {
                logger.warn("Failed to parse release date: {}", dto.getSet().getReleaseDate());
            }
        }

        return cardRepository.save(card);
    }

    private BigDecimal extractMarketPrice(PokemonCardDto.TcgPlayer.Prices prices) {
        if (prices.getHolofoil() != null && prices.getHolofoil().getMarket() != null) {
            return BigDecimal.valueOf(prices.getHolofoil().getMarket());
        }
        if (prices.getReverseHolofoil() != null && prices.getReverseHolofoil().getMarket() != null) {
            return BigDecimal.valueOf(prices.getReverseHolofoil().getMarket());
        }
        if (prices.getNormal() != null && prices.getNormal().getMarket() != null) {
            return BigDecimal.valueOf(prices.getNormal().getMarket());
        }
        return BigDecimal.ZERO;
    }
}

package com.cardvault.repository;

import com.cardvault.model.UserCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserCardRepository extends JpaRepository<UserCard, UUID> {

    List<UserCard> findByUserId(UUID userId);

    Optional<UserCard> findByUserIdAndCardId(UUID userId, UUID cardId);

    List<UserCard> findByUserIdOrderByCreatedAtDesc(UUID userId);

    @Query("SELECT uc FROM UserCard uc WHERE uc.user.id = :userId AND uc.card.rarity = :rarity")
    List<UserCard> findByUserIdAndRarity(@Param("userId") UUID userId, @Param("rarity") String rarity);

    @Query("SELECT COUNT(uc) FROM UserCard uc WHERE uc.user.id = :userId")
    Long countByUserId(@Param("userId") UUID userId);

    @Query("SELECT SUM(uc.quantity) FROM UserCard uc WHERE uc.user.id = :userId")
    Long sumQuantityByUserId(@Param("userId") UUID userId);

    @Query("SELECT SUM(uc.currentValue * uc.quantity) FROM UserCard uc WHERE uc.user.id = :userId")
    BigDecimal sumTotalValueByUserId(@Param("userId") UUID userId);

    @Query("SELECT uc FROM UserCard uc WHERE uc.user.id = :userId ORDER BY (uc.currentValue * uc.quantity) DESC")
    List<UserCard> findTopValueCardsByUserId(@Param("userId") UUID userId);

    @Query("SELECT uc FROM UserCard uc WHERE uc.user.id = :userId AND uc.card.setName = :setName")
    List<UserCard> findByUserIdAndSetName(@Param("userId") UUID userId, @Param("setName") String setName);
}

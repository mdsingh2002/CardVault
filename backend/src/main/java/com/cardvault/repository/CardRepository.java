package com.cardvault.repository;

import com.cardvault.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {

    Optional<Card> findByApiId(String apiId);

    List<Card> findByNameContainingIgnoreCase(String name);

    List<Card> findBySetName(String setName);

    List<Card> findByRarity(String rarity);

    List<Card> findByCardType(String cardType);

    @Query("SELECT c FROM Card c WHERE " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.setName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.rarity) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Card> searchCards(@Param("searchTerm") String searchTerm);

    List<Card> findBySetNameOrderByCardNumberAsc(String setName);
}

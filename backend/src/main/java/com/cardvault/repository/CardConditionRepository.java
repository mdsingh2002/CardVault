package com.cardvault.repository;

import com.cardvault.model.CardCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardConditionRepository extends JpaRepository<CardCondition, Long> {

    Optional<CardCondition> findByName(String name);

    boolean existsByName(String name);
}

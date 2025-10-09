package dev.project.ra2avaliacao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dev.project.ra2avaliacao.models.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {}

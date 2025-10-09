package dev.project.ra2avaliacao.repositories;

import dev.project.ra2avaliacao.models.CardTags;
import dev.project.ra2avaliacao.models.CardTagsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardTagsRepository extends JpaRepository<CardTags, CardTagsId> {
}

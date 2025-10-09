package dev.project.ra2avaliacao.repositories;

import dev.project.ra2avaliacao.models.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ColumnRepository extends JpaRepository<Column, String> {
    List<Column> findAllByProjectId(String projectId);
    Optional<Column> findByPositionAndProjectId(int position, String projectId);
}

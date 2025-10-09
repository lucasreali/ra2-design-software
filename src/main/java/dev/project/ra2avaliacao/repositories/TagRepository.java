package dev.project.ra2avaliacao.repositories;

import dev.project.ra2avaliacao.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {
    List<Tag> findByProjectId(String projectId);
    boolean existsByNameAndProjectId(String name, String projectId);
}

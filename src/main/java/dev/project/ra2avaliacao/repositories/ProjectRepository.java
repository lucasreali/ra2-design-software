package dev.project.ra2avaliacao.repositories;

import dev.project.ra2avaliacao.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
}

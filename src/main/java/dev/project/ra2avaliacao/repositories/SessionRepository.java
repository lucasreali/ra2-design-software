package dev.project.ra2avaliacao.repositories;


import dev.project.ra2avaliacao.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
}

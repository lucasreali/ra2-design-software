package dev.project.ra2avaliacao.models;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "projects")
@NoArgsConstructor
@Setter
@Getter
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Construtor privado que recebe o Builder
    private Project(ProjectBuilder builder) {
        this.name = builder.name;
        this.description = builder.description;
    }

    // Classe Builder estática interna
    public static class ProjectBuilder {
        private String name;
        private String description;

        public ProjectBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProjectBuilder description(String description) {
            this.description = description;
            return this;
        }

        public Project build() {
            return new Project(this);
        }
    }
}

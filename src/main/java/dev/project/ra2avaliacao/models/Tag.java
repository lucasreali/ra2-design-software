package dev.project.ra2avaliacao.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tags")
@NoArgsConstructor
@Getter
@Setter
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @jakarta.persistence.Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @CreationTimestamp
    @jakarta.persistence.Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @jakarta.persistence.Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Construtor privado que recebe o Builder
    private Tag(TagBuilder builder) {
        this.name = builder.name;
        this.project = builder.project;
    }

    // Classe Builder estática interna
    public static class TagBuilder {
        private String name;
        private Project project;

        public TagBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TagBuilder project(Project project) {
            this.project = project;
            return this;
        }

        public Tag build() {
            return new Tag(this);
        }
    }
}

package dev.project.ra2avaliacao.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "columns")
@Getter
@Setter
@NoArgsConstructor
public class Column {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @jakarta.persistence.Column(nullable = false, name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @jakarta.persistence.Column(nullable = false, name = "position")
    private Integer position;

    @OneToMany(mappedBy = "column", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Card> cards;

    @CreationTimestamp
    @jakarta.persistence.Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @jakarta.persistence.Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Construtor privado que recebe o Builder
    private Column(ColumnBuilder builder) {
        this.name = builder.name;
        this.project = builder.project;
        this.position = builder.position;
    }

    // Classe Builder estática interna
    public static class ColumnBuilder {
        private String name;
        private Project project;
        private Integer position;

        public ColumnBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ColumnBuilder project(Project project) {
            this.project = project;
            return this;
        }

        public ColumnBuilder position(Integer position) {
            this.position = position;
            return this;
        }

        public Column build() {
            return new Column(this);
        }
    }
}

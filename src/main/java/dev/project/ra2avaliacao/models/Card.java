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
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @jakarta.persistence.Column(nullable = false, name = "title")
    private String title;

    @jakarta.persistence.Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "column_id", nullable = false)
    private Column column;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CardTags> cardTags;

    @CreationTimestamp
    @jakarta.persistence.Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @jakarta.persistence.Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Construtor privado que recebe o Builder
    private Card(CardBuilder builder) {
        this.title = builder.title;
        this.content = builder.content;
        this.column = builder.column;
    }

    // Classe Builder estática interna
    public static class CardBuilder {
        private String title;
        private String content;
        private Column column;

        public CardBuilder title(String title) {
            this.title = title;
            return this;
        }

        public CardBuilder content(String content) {
            this.content = content;
            return this;
        }

        public CardBuilder column(Column column) {
            this.column = column;
            return this;
        }

        public Card build() {
            return new Card(this);
        }
    }
}

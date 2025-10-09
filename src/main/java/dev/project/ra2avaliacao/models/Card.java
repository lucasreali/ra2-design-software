package dev.project.ra2avaliacao.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "cards")
@Getter
@Setter
@AllArgsConstructor
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

    @CreationTimestamp
    @jakarta.persistence.Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @jakarta.persistence.Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

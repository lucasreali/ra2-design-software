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
@Table(name = "project_participants")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProjectParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @jakarta.persistence.Column(name = "role", nullable = false)
    private ParticipantRole role;

    @CreationTimestamp
    @jakarta.persistence.Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @jakarta.persistence.Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}


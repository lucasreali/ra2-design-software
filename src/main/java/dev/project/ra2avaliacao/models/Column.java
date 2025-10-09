package dev.project.ra2avaliacao.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Table(name = "columns")
public class Columns {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id
}

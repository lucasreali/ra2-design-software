package dev.project.ra2avaliacao.dtos.column;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ColumnResponseDTO {
    private String id;
    private String name;
    private Integer position;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

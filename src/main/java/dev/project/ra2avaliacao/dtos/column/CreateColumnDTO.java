package dev.project.ra2avaliacao.dtos.column;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateColumnDTO {
    @NotBlank(message = "Column name is required")
    private String name;

    @NotNull(message = "Position is required")
    @Min(value = 1, message = "Position must be greater than 0")
    private Integer position;

}

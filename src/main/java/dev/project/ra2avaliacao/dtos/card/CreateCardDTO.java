package dev.project.ra2avaliacao.dtos.card;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCardDTO {

    @NotNull(message = "Title is required")
    @NotBlank(message = "Title cannot be blank")
    private String title;

    private String content;
}

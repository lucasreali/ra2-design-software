package dev.project.ra2avaliacao.services;

import dev.project.ra2avaliacao.dtos.card.CardResponseDTO;
import dev.project.ra2avaliacao.dtos.card.CreateCardDTO;
import dev.project.ra2avaliacao.models.Card;
import dev.project.ra2avaliacao.models.Column;
import dev.project.ra2avaliacao.repositories.CardRepository;
import dev.project.ra2avaliacao.repositories.ColumnRepository;
import org.springframework.stereotype.Service;


@Service
public class CardService {
    private final CardRepository cardRepository;
    private final ColumnRepository columnRepository;
    private final ProjectPermissionValidator permissionValidator;

    public CardService (CardRepository cardRepository, ProjectPermissionValidator permissionValidator, ColumnRepository columnRepository) {
        this.cardRepository = cardRepository;
        this.permissionValidator = permissionValidator;
        this.columnRepository = columnRepository;
    }

    public CardResponseDTO create(String columnId, CreateCardDTO createCardDTO, String userId) {


        Column column = columnRepository.findById(columnId)
                .orElseThrow(() -> new RuntimeException("Column not found"));

        if (permissionValidator.isMember(column.getProject().getId() ,userId)) {
            throw new RuntimeException("User does not have permission to edit this project");
        }


        Card card = new Card();
        card.setTitle(createCardDTO.getTitle());
        card.setColumn(column);

        if (!createCardDTO.getContent().isEmpty()) {
            card.setContent(createCardDTO.getContent());
        }

        cardRepository.save(card);
        return convertToResponseDto(card);
    }

    public void delete(String cardId, String userId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        String projectId = card.getColumn().getProject().getId();
        if (permissionValidator.isMember(projectId ,userId)) {
            throw new RuntimeException("User does not have permission to edit this project");
        }

        cardRepository.delete(card);
    }

    private CardResponseDTO convertToResponseDto(Card card) {
        CardResponseDTO dto = new CardResponseDTO();
        dto.setId(card.getId());
        dto.setTitle(card.getTitle());
        dto.setContent(card.getContent());
        return dto;
    }
}

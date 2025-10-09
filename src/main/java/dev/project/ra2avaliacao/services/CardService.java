package dev.project.ra2avaliacao.services;

import dev.project.ra2avaliacao.dtos.card.CardResponseDTO;
import dev.project.ra2avaliacao.dtos.card.CreateCardDTO;
import dev.project.ra2avaliacao.dtos.card.UpdateCardDTO;
import dev.project.ra2avaliacao.models.Card;
import dev.project.ra2avaliacao.models.Column;
import dev.project.ra2avaliacao.repositories.CardRepository;
import dev.project.ra2avaliacao.repositories.ColumnRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {
    private final CardRepository cardRepository;
    private final ColumnRepository columnRepository;
    private final ProjectPermissionValidator permissionValidator;

    public CardService(CardRepository cardRepository, ProjectPermissionValidator permissionValidator, ColumnRepository columnRepository) {
        this.cardRepository = cardRepository;
        this.permissionValidator = permissionValidator;
        this.columnRepository = columnRepository;
    }

    public CardResponseDTO create(String columnId, CreateCardDTO createCardDTO, String userId) {
        Column targetColumn = columnRepository.findById(columnId)
                .orElseThrow(() -> new RuntimeException("Column not found"));

        if (!permissionValidator.isMember(targetColumn.getProject().getId(), userId)) {
            throw new RuntimeException("User does not have permission to edit this project");
        }

        Card newCard = new Card();
        newCard.setTitle(createCardDTO.getTitle());
        newCard.setColumn(targetColumn);

        if (createCardDTO.getContent() != null && !createCardDTO.getContent().isEmpty()) {
            newCard.setContent(createCardDTO.getContent());
        }

        Card savedCard = cardRepository.save(newCard);
        return convertToResponseDto(savedCard);
    }

    public CardResponseDTO findById(String cardId, String userId) {
        Card foundCard = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        String projectId = foundCard.getColumn().getProject().getId();
        if (!permissionValidator.isMember(projectId, userId)) {
            throw new RuntimeException("User does not have permission to view this project");
        }

        return convertToResponseDto(foundCard);
    }

    public List<CardResponseDTO> findByColumnId(String columnId, String userId) {
        Column targetColumn = columnRepository.findById(columnId)
                .orElseThrow(() -> new RuntimeException("Column not found"));

        if (!permissionValidator.isMember(targetColumn.getProject().getId(), userId)) {
            throw new RuntimeException("User does not have permission to view this project");
        }

        List<Card> cards = cardRepository.findByColumnId(columnId);
        return cards.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public CardResponseDTO update(String cardId, UpdateCardDTO updateCardDTO, String userId) {
        Card existingCard = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        String projectId = existingCard.getColumn().getProject().getId();
        if (!permissionValidator.isMember(projectId, userId)) {
            throw new RuntimeException("User does not have permission to edit this project");
        }

        existingCard.setTitle(updateCardDTO.getTitle());

        if (updateCardDTO.getContent() != null) {
            existingCard.setContent(updateCardDTO.getContent());
        }

        Card updatedCard = cardRepository.save(existingCard);
        return convertToResponseDto(updatedCard);
    }

    public void delete(String cardId, String userId) {
        Card cardToDelete = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        String projectId = cardToDelete.getColumn().getProject().getId();
        if (!permissionValidator.isMember(projectId, userId)) {
            throw new RuntimeException("User does not have permission to edit this project");
        }

        cardRepository.delete(cardToDelete);
    }

    private CardResponseDTO convertToResponseDto(Card card) {
        CardResponseDTO responseDto = new CardResponseDTO();
        responseDto.setId(card.getId());
        responseDto.setTitle(card.getTitle());
        responseDto.setContent(card.getContent());
        responseDto.setCreatedAt(card.getCreatedAt());
        responseDto.setUpdatedAt(card.getUpdatedAt());
        return responseDto;
    }
}

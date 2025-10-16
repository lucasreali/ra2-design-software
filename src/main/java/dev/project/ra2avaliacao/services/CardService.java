package dev.project.ra2avaliacao.services;

import dev.project.ra2avaliacao.dtos.card.CardResponseDTO;
import dev.project.ra2avaliacao.dtos.card.CreateCardDTO;
import dev.project.ra2avaliacao.dtos.card.UpdateCardDTO;
import dev.project.ra2avaliacao.models.Card;
import dev.project.ra2avaliacao.models.CardTags;
import dev.project.ra2avaliacao.models.CardTagsId;
import dev.project.ra2avaliacao.models.Column;
import dev.project.ra2avaliacao.models.Tag;
import dev.project.ra2avaliacao.repositories.CardRepository;
import dev.project.ra2avaliacao.repositories.CardTagsRepository;
import dev.project.ra2avaliacao.repositories.ColumnRepository;
import dev.project.ra2avaliacao.repositories.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {
    private final CardRepository cardRepository;
    private final ColumnRepository columnRepository;
    private final CardTagsRepository cardTagsRepository;
    private final TagRepository tagRepository;
    private final ProjectPermissionValidator permissionValidator;

    public CardService(CardRepository cardRepository, ProjectPermissionValidator permissionValidator,
                      ColumnRepository columnRepository, CardTagsRepository cardTagsRepository,
                      TagRepository tagRepository) {
        this.cardRepository = cardRepository;
        this.permissionValidator = permissionValidator;
        this.columnRepository = columnRepository;
        this.cardTagsRepository = cardTagsRepository;
        this.tagRepository = tagRepository;
    }

    public CardResponseDTO create(String columnId, CreateCardDTO createCardDTO, String userId) {
        Column targetColumn = columnRepository.findById(columnId)
                .orElseThrow(() -> new RuntimeException("Column not found"));

        if (!permissionValidator.isMember(targetColumn.getProject().getId(), userId)) {
            throw new RuntimeException("User does not have permission to edit this project");
        }

        Card.CardBuilder cardBuilder = new Card.CardBuilder()
                .title(createCardDTO.getTitle())
                .column(targetColumn);

        if (createCardDTO.getContent() != null && !createCardDTO.getContent().isEmpty()) {
            cardBuilder.content(createCardDTO.getContent());
        }

        Card newCard = cardBuilder.build();
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

    public void assignTagToCard(String cardId, String tagId, String userId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        String projectId = card.getColumn().getProject().getId();
        if (!permissionValidator.isMember(projectId, userId)) {
            throw new RuntimeException("User does not have permission to edit this project");
        }

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        // Verificar se a tag pertence ao mesmo projeto do card
        if (!tag.getProject().getId().equals(projectId)) {
            throw new RuntimeException("Tag does not belong to the same project as the card");
        }

        // Verificar se a tag já está atribuída ao card
        CardTagsId cardTagsId = new CardTagsId(cardId, tagId);
        if (cardTagsRepository.existsById(cardTagsId)) {
            throw new RuntimeException("Tag is already assigned to this card");
        }

        CardTags cardTags = new CardTags();
        cardTags.setId(cardTagsId);
        cardTags.setCard(card);
        cardTags.setTag(tag);

        cardTagsRepository.save(cardTags);
    }

    public void removeTagFromCard(String cardId, String tagId, String userId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        String projectId = card.getColumn().getProject().getId();
        if (!permissionValidator.isMember(projectId, userId)) {
            throw new RuntimeException("User does not have permission to edit this project");
        }

        CardTagsId cardTagsId = new CardTagsId(cardId, tagId);
        if (!cardTagsRepository.existsById(cardTagsId)) {
            throw new RuntimeException("Tag is not assigned to this card");
        }

        cardTagsRepository.deleteById(cardTagsId);
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

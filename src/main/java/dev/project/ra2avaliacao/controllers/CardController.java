package dev.project.ra2avaliacao.controllers;

import dev.project.ra2avaliacao.dtos.card.CardResponseDTO;
import dev.project.ra2avaliacao.dtos.card.CreateCardDTO;
import dev.project.ra2avaliacao.dtos.card.UpdateCardDTO;
import dev.project.ra2avaliacao.models.User;
import dev.project.ra2avaliacao.services.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("cards")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/column/{columnId}")
    public ResponseEntity<CardResponseDTO> create(@PathVariable String columnId,
                                                @Valid @RequestBody CreateCardDTO createCardDTO,
                                                @AuthenticationPrincipal User user) {
        CardResponseDTO cardResponseDTO = cardService.create(columnId, createCardDTO, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(cardResponseDTO);
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<CardResponseDTO> findById(@PathVariable String cardId,
                                                  @AuthenticationPrincipal User user) {
        CardResponseDTO cardResponseDTO = cardService.findById(cardId, user.getId());
        return ResponseEntity.ok(cardResponseDTO);
    }

    @GetMapping("/column/{columnId}")
    public ResponseEntity<List<CardResponseDTO>> findByColumnId(@PathVariable String columnId,
                                                               @AuthenticationPrincipal User user) {
        List<CardResponseDTO> cards = cardService.findByColumnId(columnId, user.getId());
        return ResponseEntity.ok(cards);
    }

    @PutMapping("/{cardId}")
    public ResponseEntity<CardResponseDTO> update(@PathVariable String cardId,
                                                @Valid @RequestBody UpdateCardDTO updateCardDTO,
                                                @AuthenticationPrincipal User user) {
        CardResponseDTO cardResponseDTO = cardService.update(cardId, updateCardDTO, user.getId());
        return ResponseEntity.ok(cardResponseDTO);
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> delete(@PathVariable String cardId, @AuthenticationPrincipal User user) {
        cardService.delete(cardId, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{cardId}/tags/{tagId}")
    public ResponseEntity<Void> assignTag(@PathVariable String cardId,
                                         @PathVariable String tagId,
                                         @AuthenticationPrincipal User user) {
        try {
            cardService.assignTagToCard(cardId, tagId, user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/{cardId}/tags/{tagId}")
    public ResponseEntity<Void> removeTag(@PathVariable String cardId,
                                         @PathVariable String tagId,
                                         @AuthenticationPrincipal User user) {
        try {
            cardService.removeTagFromCard(cardId, tagId, user.getId());
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}

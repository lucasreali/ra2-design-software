package dev.project.ra2avaliacao.controllers;

import dev.project.ra2avaliacao.dtos.card.CardResponseDTO;
import dev.project.ra2avaliacao.dtos.card.CreateCardDTO;
import dev.project.ra2avaliacao.models.User;
import dev.project.ra2avaliacao.services.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cards")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/column/{columnId}")
    public ResponseEntity<CardResponseDTO> create(@PathVariable String columnId, @RequestBody CreateCardDTO createCardDTO, @AuthenticationPrincipal User user) {
        CardResponseDTO columnResponseDTO = cardService.create(columnId, createCardDTO, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(columnResponseDTO);
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> delete(@PathVariable String cardId, @AuthenticationPrincipal User user) {
        cardService.delete(cardId, user.getId());
        return ResponseEntity.noContent().build();
    }
}

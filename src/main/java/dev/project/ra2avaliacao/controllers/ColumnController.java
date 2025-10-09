package dev.project.ra2avaliacao.controllers;

import dev.project.ra2avaliacao.dtos.column.ColumnResponseDTO;
import dev.project.ra2avaliacao.dtos.column.CreateColumnDTO;
import dev.project.ra2avaliacao.dtos.column.ReorderColumnDTO;
import dev.project.ra2avaliacao.dtos.column.UpdateColumnDTO;
import dev.project.ra2avaliacao.models.User;
import dev.project.ra2avaliacao.services.ColumnService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/columns")
public class ColumnController {

    private final ColumnService columnService;
    public ColumnController(ColumnService columnService) {
        this.columnService = columnService;
    }

    @PostMapping("/project/{projectId}")
    public ResponseEntity<ColumnResponseDTO> create(
            @PathVariable String projectId,
            @Valid @RequestBody CreateColumnDTO createColumnDTO,
            @AuthenticationPrincipal User user) {
        try {
            ColumnResponseDTO columnResponse = columnService.create(projectId, createColumnDTO, user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(columnResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/{columnId}")
    public ResponseEntity<ColumnResponseDTO> update(
            @PathVariable String columnId,
            @Valid @RequestBody UpdateColumnDTO updateColumnDTO,
            @AuthenticationPrincipal User user) {
        try {
            ColumnResponseDTO columnResponse = columnService.update(columnId, updateColumnDTO, user.getId());
            return ResponseEntity.ok(columnResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PatchMapping("/{columnId}/position")
    public ResponseEntity<Void> changeColumnPosition(
            @PathVariable String columnId,
            @Valid @RequestBody ReorderColumnDTO reorderColumnDTO,
            @AuthenticationPrincipal User user) {
        try {
            columnService.changePosition(columnId, reorderColumnDTO.getNewPosition(), user.getId());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/columns/{columnId}")
    public ResponseEntity<Void> delete(
            @PathVariable String columnId,
            @AuthenticationPrincipal User user) {
        try {
            columnService.delete(columnId, user.getId());
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}

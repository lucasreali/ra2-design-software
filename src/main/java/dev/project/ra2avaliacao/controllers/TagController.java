package dev.project.ra2avaliacao.controllers;

import dev.project.ra2avaliacao.dtos.tag.CreateTagDTO;
import dev.project.ra2avaliacao.dtos.tag.TagResponseDTO;
import dev.project.ra2avaliacao.dtos.tag.UpdateTagDTO;
import dev.project.ra2avaliacao.models.User;
import dev.project.ra2avaliacao.services.TagService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<TagResponseDTO> create(
            @Valid @RequestBody CreateTagDTO createTagDTO,
            @AuthenticationPrincipal User user) {
        try {
            TagResponseDTO createdTag = tagService.create(createTagDTO, user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTag);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagResponseDTO> getById(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {
        try {
            TagResponseDTO tag = tagService.getById(id, user.getId());
            if (tag == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(tag);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TagResponseDTO>> getAllByProjectId(
            @PathVariable String projectId,
            @AuthenticationPrincipal User user) {
        try {
            List<TagResponseDTO> tags = tagService.getAllByProjectId(projectId, user.getId());
            return ResponseEntity.ok(tags);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagResponseDTO> update(
            @PathVariable String id,
            @Valid @RequestBody UpdateTagDTO updateTagDTO,
            @AuthenticationPrincipal User user) {
        try {
            TagResponseDTO updatedTag = tagService.update(id, updateTagDTO, user.getId());
            if (updatedTag == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedTag);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {
        try {
            tagService.delete(id, user.getId());
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}


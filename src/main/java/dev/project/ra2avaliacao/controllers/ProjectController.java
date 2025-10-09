package dev.project.ra2avaliacao.controllers;

import dev.project.ra2avaliacao.dtos.project.*;
import dev.project.ra2avaliacao.models.User;
import dev.project.ra2avaliacao.services.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> create(
            @Valid @RequestBody CreateProjectDTO createProjectDTO,
            @AuthenticationPrincipal User user) {

        ProjectResponseDTO createdProject = projectService.create(createProjectDTO, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects(@AuthenticationPrincipal User user) {

        List<ProjectResponseDTO> projects = projectService.getAllProjects(user.getId());
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {
        try {
            ProjectResponseDTO project = projectService.getById(id, user.getId());
            if (project == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(project);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProjectResponseDTO>> getProjectsByUserId(
            @PathVariable String userId,
            @AuthenticationPrincipal User user) {
        if (!userId.equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<ProjectResponseDTO> projects = projectService.getProjectsByUserId(userId);
        return ResponseEntity.ok(projects);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> updateProject(
            @PathVariable String id,
            @Valid @RequestBody UpdateProjectDTO updateProjectDTO,
            @AuthenticationPrincipal User user) {
        try {
            ProjectResponseDTO updatedProject = projectService.update(id, updateProjectDTO, user.getId());
            if (updatedProject == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedProject);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {
        try {
            projectService.delete(id, user.getId());
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/{projectId}/participants")
    public ResponseEntity<ParticipantResponseDTO> addParticipant(
            @PathVariable String projectId,
            @Valid @RequestBody AddParticipantDTO addParticipantDTO,
            @AuthenticationPrincipal User user) {
        try {
            ParticipantResponseDTO participant = projectService.addParticipant(projectId, addParticipantDTO, user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(participant);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{projectId}/participants/{userId}/role")
    public ResponseEntity<ParticipantResponseDTO> updateParticipantRole(
            @PathVariable String projectId,
            @PathVariable String userId,
            @Valid @RequestBody UpdateParticipantRoleDTO updateRoleDTO,
            @AuthenticationPrincipal User user) {
        try {
            ParticipantResponseDTO participant = projectService.updateParticipantRole(projectId, userId, updateRoleDTO, user.getId());
            return ResponseEntity.ok(participant);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/{projectId}/participants/{userId}")
    public ResponseEntity<Void> removeParticipant(
            @PathVariable String projectId,
            @PathVariable String userId,
            @AuthenticationPrincipal User user) {
        try {
            projectService.removeParticipant(projectId, userId, user.getId());
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}

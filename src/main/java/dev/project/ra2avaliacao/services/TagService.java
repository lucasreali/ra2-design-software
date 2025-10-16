package dev.project.ra2avaliacao.services;

import dev.project.ra2avaliacao.dtos.tag.CreateTagDTO;
import dev.project.ra2avaliacao.dtos.tag.TagResponseDTO;
import dev.project.ra2avaliacao.dtos.tag.UpdateTagDTO;
import dev.project.ra2avaliacao.models.Project;
import dev.project.ra2avaliacao.models.Tag;
import dev.project.ra2avaliacao.repositories.ProjectRepository;
import dev.project.ra2avaliacao.repositories.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagService {
    private final TagRepository tagRepository;
    private final ProjectRepository projectRepository;
    private final ProjectPermissionValidator permissionValidator;

    public TagService(TagRepository tagRepository, ProjectRepository projectRepository, ProjectPermissionValidator permissionValidator) {
        this.tagRepository = tagRepository;
        this.projectRepository = projectRepository;
        this.permissionValidator = permissionValidator;
    }

    public TagResponseDTO create(CreateTagDTO createTagDTO, String userId) {
        validateUserPermission(createTagDTO.getProjectId(), userId);

        Optional<Project> projectOptional = projectRepository.findById(createTagDTO.getProjectId());
        if (projectOptional.isEmpty()) {
            throw new RuntimeException("Project not found");
        }

        if (tagRepository.existsByNameAndProjectId(createTagDTO.getName(), createTagDTO.getProjectId())) {
            throw new RuntimeException("Tag with this name already exists in this project");
        }

        Tag tag = new Tag.TagBuilder()
                .name(createTagDTO.getName())
                .project(projectOptional.get())
                .build();

        Tag savedTag = tagRepository.save(tag);

        return convertToResponseDTO(savedTag);
    }

    public TagResponseDTO getById(String id, String userId) {
        Tag tag = findTagByIdAndValidatePermission(id, userId);
        return convertToResponseDTO(tag);
    }

    public List<TagResponseDTO> getAllByProjectId(String projectId, String userId) {
        validateUserPermission(projectId, userId);

        List<Tag> tags = tagRepository.findByProjectId(projectId);
        return tags.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public TagResponseDTO update(String id, UpdateTagDTO updateTagDTO, String userId) {
        Tag tag = findTagByIdAndValidatePermission(id, userId);

        if (!tag.getName().equals(updateTagDTO.getName()) &&
            tagRepository.existsByNameAndProjectId(updateTagDTO.getName(), tag.getProject().getId())) {
            throw new RuntimeException("Tag with this name already exists in this project");
        }

        tag.setName(updateTagDTO.getName());
        Tag updatedTag = tagRepository.save(tag);

        return convertToResponseDTO(updatedTag);
    }

    public void delete(String id, String userId) {
        Tag tag = findTagByIdAndValidatePermission(id, userId);
        tagRepository.deleteById(id);
    }

    private void validateUserPermission(String projectId, String userId) {
        if (!permissionValidator.isParticipant(projectId, userId)) {
            throw new RuntimeException("User is not a participant of this project");
        }
    }

    private Tag findTagByIdAndValidatePermission(String id, String userId) {
        Optional<Tag> tagOptional = tagRepository.findById(id);
        if (tagOptional.isEmpty()) {
            throw new RuntimeException("Tag not found");
        }

        Tag tag = tagOptional.get();
        validateUserPermission(tag.getProject().getId(), userId);

        return tag;
    }

    private TagResponseDTO convertToResponseDTO(Tag tag) {
        TagResponseDTO dto = new TagResponseDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        dto.setProjectId(tag.getProject().getId());
        dto.setProjectName(tag.getProject().getName());
        dto.setCreatedAt(tag.getCreatedAt());
        dto.setUpdatedAt(tag.getUpdatedAt());
        return dto;
    }
}

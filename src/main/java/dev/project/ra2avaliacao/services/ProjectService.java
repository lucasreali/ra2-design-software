package dev.project.ra2avaliacao.services;

import dev.project.ra2avaliacao.dtos.project.*;
import dev.project.ra2avaliacao.models.ParticipantRole;
import dev.project.ra2avaliacao.models.Project;
import dev.project.ra2avaliacao.models.ProjectParticipant;
import dev.project.ra2avaliacao.models.User;
import dev.project.ra2avaliacao.repositories.ProjectParticipantRepository;
import dev.project.ra2avaliacao.repositories.ProjectRepository;
import dev.project.ra2avaliacao.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectParticipantRepository projectParticipantRepository;
    private final ProjectPermissionValidator permissionValidator;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository,
                         ProjectParticipantRepository projectParticipantRepository,
                         ProjectPermissionValidator permissionValidator) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectParticipantRepository = projectParticipantRepository;
        this.permissionValidator = permissionValidator;
    }

    public ProjectResponseDTO create(CreateProjectDTO createProjectDTO, String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();

        Project project = new Project.ProjectBuilder()
                .name(createProjectDTO.getName())
                .description(createProjectDTO.getDescription())
                .build();

        Project savedProject = projectRepository.save(project);

        ProjectParticipant participant = new ProjectParticipant();
        participant.setProject(savedProject);
        participant.setUser(user);
        participant.setRole(ParticipantRole.CREATOR);
        projectParticipantRepository.save(participant);

        return convertToResponseDTO(savedProject);
    }

    public ProjectResponseDTO getById(String id, String userId) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        if (projectOptional.isEmpty()) {
            return null;
        }

        if (!permissionValidator.isParticipant(id, userId)) {
            throw new RuntimeException("User is not a participant of this project");
        }

        return convertToResponseDTO(projectOptional.get());
    }

    public List<ProjectResponseDTO> getAllProjects(String userId) {
        List<ProjectParticipant> participants = projectParticipantRepository.findByUserId(userId);
        return participants.stream().map(participant -> convertToResponseDTO(participant.getProject())).collect(Collectors.toList());
    }

    public List<ProjectResponseDTO> getProjectsByUserId(String userId) {
        List<ProjectParticipant> participants = projectParticipantRepository.findByUserId(userId);
        return participants.stream().map(participant -> convertToResponseDTO(participant.getProject())).collect(Collectors.toList());
    }

    public ProjectResponseDTO update(String id, UpdateProjectDTO updateProjectDTO, String userId) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isEmpty()) {
            return null;
        }

        if (permissionValidator.isMember(id, userId)) {
            throw new RuntimeException("User does not have permission to edit this project");
        }

        Project project = optionalProject.get();
        project.setName(updateProjectDTO.getName());
        project.setDescription(updateProjectDTO.getDescription());

        Project updatedProject = projectRepository.save(project);

        return convertToResponseDTO(updatedProject);
    }

    public void delete(String id, String userId) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project not found");
        }

        if (!permissionValidator.isCreator(id, userId)) {
            throw new RuntimeException("User does not have permission to delete this project");
        }

        projectRepository.deleteById(id);
    }

    public ParticipantResponseDTO addParticipant(String projectId, AddParticipantDTO addParticipantDTO, String userId) {
        Optional<Project> projectOptional = projectRepository.findById(projectId);
        if (projectOptional.isEmpty()) {
            throw new RuntimeException("Project not found");
        }

        if (permissionValidator.isMember(projectId, userId)) {
            throw new RuntimeException("User does not have permission to manage participants of this project");
        }

        Optional<User> userOptional = userRepository.findById(addParticipantDTO.getUserId());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Project project = projectOptional.get();
        User user = userOptional.get();

        Optional<ProjectParticipant> existingParticipant = projectParticipantRepository.findByProjectIdAndUserId(projectId, addParticipantDTO.getUserId());

        if (existingParticipant.isPresent()) {
            throw new RuntimeException("User is already a participant of this project");
        }

        ProjectParticipant participant = new ProjectParticipant();
        participant.setProject(project);
        participant.setUser(user);
        participant.setRole(ParticipantRole.MEMBER);

        ProjectParticipant savedParticipant = projectParticipantRepository.save(participant);

        return convertToParticipantDTO(savedParticipant);
    }

    public ParticipantResponseDTO updateParticipantRole(String projectId, String participantUserId, UpdateParticipantRoleDTO updateRoleDTO, String currentUserId) {
        if (!permissionValidator.isCreator(projectId, currentUserId)) {
            throw new RuntimeException("User does not have permission to manage roles in this project");
        }

        ProjectParticipant participant = projectParticipantRepository
            .findByProjectIdAndUserId(projectId, participantUserId)
            .orElseThrow(() -> new RuntimeException("Participant not found in this project"));

        if (participant.getRole() == ParticipantRole.CREATOR) {
            throw new RuntimeException("Cannot change the role of the project creator");
        }

        // Aplica o padrão State aqui 👇
        if (updateRoleDTO.getRole() == ParticipantRole.ADMIN) {
            participant.promote();
        } else if (updateRoleDTO.getRole() == ParticipantRole.MEMBER) {
            participant.demote();
        }

        ProjectParticipant updatedParticipant = projectParticipantRepository.save(participant);
        return convertToParticipantDTO(updatedParticipant);
    }

    public void removeParticipant(String projectId, String participantUserId, String currentUserId) {
        if (permissionValidator.isMember(projectId, currentUserId)) {
            throw new RuntimeException("User does not have permission to manage participants of this project");
        }

        Optional<ProjectParticipant> participantOptional = projectParticipantRepository.findByProjectIdAndUserId(projectId, participantUserId);

        if (participantOptional.isEmpty()) {
            throw new RuntimeException("Participant not found in this project");
        }

        ProjectParticipant participant = participantOptional.get();

        if (participant.getRole() == ParticipantRole.CREATOR) {
            throw new RuntimeException("Cannot remove the project creator");
        }

        projectParticipantRepository.delete(participant);
    }

    private ProjectResponseDTO convertToResponseDTO(Project project) {
        ProjectResponseDTO dto = new ProjectResponseDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());

        List<ProjectParticipant> participants = projectParticipantRepository.findByProjectId(project.getId());

        List<ParticipantResponseDTO> participantDTOs = participants.stream().map(this::convertToParticipantDTO).collect(Collectors.toList());

        dto.setParticipants(participantDTOs);

        Optional<ProjectParticipant> creator = participants.stream().filter(p -> p.getRole() == ParticipantRole.CREATOR).findFirst();

        if (creator.isPresent()) {
            User creatorUser = creator.get().getUser();
            dto.setUserId(creatorUser.getId());
            dto.setUserName(creatorUser.getName());
            dto.setUserEmail(creatorUser.getEmail());
        }

        dto.setCreatedAt(project.getCreatedAt());
        dto.setUpdatedAt(project.getUpdatedAt());
        return dto;
    }

    private ParticipantResponseDTO convertToParticipantDTO(ProjectParticipant participant) {
        ParticipantResponseDTO dto = new ParticipantResponseDTO();
        dto.setId(participant.getId());
        dto.setUserId(participant.getUser().getId());
        dto.setUserName(participant.getUser().getName());
        dto.setUserEmail(participant.getUser().getEmail());
        dto.setRole(participant.getRole());
        dto.setCreatedAt(participant.getCreatedAt());
        dto.setUpdatedAt(participant.getUpdatedAt());
        return dto;
    }
}

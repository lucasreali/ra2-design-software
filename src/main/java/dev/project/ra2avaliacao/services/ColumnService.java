package dev.project.ra2avaliacao.services;

import dev.project.ra2avaliacao.dtos.column.ColumnResponseDTO;
import dev.project.ra2avaliacao.dtos.column.CreateColumnDTO;
import dev.project.ra2avaliacao.dtos.column.UpdateColumnDTO;
import dev.project.ra2avaliacao.models.Column;
import dev.project.ra2avaliacao.models.Project;
import dev.project.ra2avaliacao.repositories.ColumnRepository;
import dev.project.ra2avaliacao.repositories.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ColumnService {
    private final ColumnRepository columnRepository;
    private final ProjectRepository projectRepository;
    private final ProjectPermissionValidator permissionValidator;

    public ColumnService(ColumnRepository columnRepository,
                         ProjectRepository projectRepository,
                         ProjectPermissionValidator permissionValidator) {
        this.columnRepository = columnRepository;
        this.projectRepository = projectRepository;
        this.permissionValidator = permissionValidator;
    }

    public ColumnResponseDTO create(String projectId, CreateColumnDTO createColumnDTO, String userId) {
        verifyPermission(projectId, userId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        List<Column> columns = columnRepository.findAllByProjectId(projectId);

        Column column = new Column.ColumnBuilder()
                .name(createColumnDTO.getName())
                .position(columns.size() + 1)
                .project(project)
                .build();

        columnRepository.save(column);

        return convertToResponseDTO(column);
    }

    public ColumnResponseDTO update(String columnId, UpdateColumnDTO updateColumnDTO, String userId) {
        Column column = columnRepository.findById(columnId)
                .orElseThrow(() -> new RuntimeException("Column not found"));

        String projectId = column.getProject().getId();
        verifyPermission(projectId, userId);

        column.setName(updateColumnDTO.getName());
        columnRepository.save(column);

        return convertToResponseDTO(column);
    }

    public void delete(String columnId, String userId) {
        Column column = columnRepository.findById(columnId)
                .orElseThrow(() -> new RuntimeException("Column not found"));

        String projectId = column.getProject().getId();
        verifyPermission(projectId, userId);

        columnRepository.delete(column);
    }

    public void changePosition(String columnId, int newPosition, String userId) {
        Column columnToMove = columnRepository.findById(columnId)
                .orElseThrow(() -> new RuntimeException("Column not found"));

        String projectId = columnToMove.getProject().getId();
        verifyPermission(projectId, userId);

        Column columnAtTargetPosition = columnRepository.findByPositionAndProjectId(newPosition, projectId)
                .orElseThrow(() -> new RuntimeException("Column not found at the new position"));

        int oldPosition = columnToMove.getPosition();
        columnAtTargetPosition.setPosition(oldPosition);
        columnToMove.setPosition(newPosition);

        columnRepository.save(columnToMove);
        columnRepository.save(columnAtTargetPosition);
    }

    private ColumnResponseDTO convertToResponseDTO(Column column) {
        ColumnResponseDTO dto = new ColumnResponseDTO();
        dto.setId(column.getId());
        dto.setName(column.getName());
        dto.setPosition(column.getPosition());
        dto.setUpdatedAt(column.getUpdatedAt());
        dto.setCreatedAt(column.getCreatedAt());
        return dto;
    }

    private void verifyPermission(String projectId, String userId) {
        if (permissionValidator.isMember(projectId, userId)) {
            throw new RuntimeException("User does not have permission to edit this project");
        }
    }
}

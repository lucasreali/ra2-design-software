package dev.project.ra2avaliacao.observers;

import dev.project.ra2avaliacao.models.Card;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CardAuditObserver implements Observer {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void update(String eventType, Object data) {
        if (data instanceof Card card) {
            String timestamp = LocalDateTime.now().format(formatter);
            String projectId = card.getColumn().getProject().getId();
            String columnName = card.getColumn().getName();

            String auditLog = String.format(
                "[AUDIT] %s | Event: %s | Project: %s | Column: %s | Card: %s (ID: %s)",
                timestamp, eventType, projectId, columnName, card.getTitle(), card.getId()
            );

            System.out.println(auditLog);
        }
    }


}

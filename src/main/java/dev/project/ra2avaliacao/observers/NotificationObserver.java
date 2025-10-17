package dev.project.ra2avaliacao.observers;

import dev.project.ra2avaliacao.models.Card;
import dev.project.ra2avaliacao.models.ProjectParticipant;
import org.springframework.stereotype.Component;

@Component
public class NotificationObserver implements Observer {

    @Override
    public void update(String eventType, Object data) {
        if (data instanceof Card card) {
            String projectId = card.getColumn().getProject().getId();

            // Simular envio de notificações baseado no tipo de evento
            switch (eventType) {
                case "CARD_CREATED" -> notifyCardCreated(card, projectId);
                case "CARD_UPDATED" -> notifyCardUpdated(card, projectId);
                case "CARD_DELETED" -> notifyCardDeleted(card, projectId);
                case "CARD_MOVED" -> notifyCardMoved(card, projectId);
                case "TAG_ASSIGNED" -> notifyTagAssigned(card, projectId);
                case "TAG_REMOVED" -> notifyTagRemoved(card, projectId);
            }
        }
    }

    private void notifyCardCreated(Card card, String projectId) {
        String message = String.format("Novo card criado: '%s' no projeto", card.getTitle());
        sendNotificationToParticipants(projectId, message);
    }

    private void notifyCardUpdated(Card card, String projectId) {
        String message = String.format("Card '%s' foi atualizado", card.getTitle());
        sendNotificationToParticipants(projectId, message);
    }

    private void notifyCardDeleted(Card card, String projectId) {
        String message = String.format("Card '%s' foi removido", card.getTitle());
        sendNotificationToParticipants(projectId, message);
    }

    private void notifyCardMoved(Card card, String projectId) {
        String message = String.format("Card '%s' foi movido para '%s'",
            card.getTitle(), card.getColumn().getName());
        sendNotificationToParticipants(projectId, message);
    }

    private void notifyTagAssigned(Card card, String projectId) {
        String message = String.format("Tag foi atribuída ao card '%s'", card.getTitle());
        sendNotificationToParticipants(projectId, message);
    }

    private void notifyTagRemoved(Card card, String projectId) {
        String message = String.format("Tag foi removida do card '%s'", card.getTitle());
        sendNotificationToParticipants(projectId, message);
    }

    private void sendNotificationToParticipants(String projectId, String message) {
        System.out.println("[NOTIFICATION] " + message + " (Projeto: " + projectId + ")");

        // Implementação futura: buscar participantes do projeto e enviar notificações
        // List<ProjectParticipant> participants = projectParticipantRepository.findByProjectId(projectId);
        // for (ProjectParticipant participant : participants) {
        //     emailService.sendNotification(participant.getUser().getEmail(), message);
        //     pushNotificationService.send(participant.getUser().getId(), message);
        // }
    }
}

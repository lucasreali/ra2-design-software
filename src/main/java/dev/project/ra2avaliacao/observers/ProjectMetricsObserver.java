package dev.project.ra2avaliacao.observers;

import dev.project.ra2avaliacao.models.Card;
import org.springframework.stereotype.Component;

@Component
public class ProjectMetricsObserver implements Observer {
    @Override
    public void update(String eventType, Object data) {
        if (data instanceof Card card) {
            System.out.println("[Observer] Evento: " + eventType + " no card " + card.getTitle());

            // Aqui você pode implementar lógicas específicas baseadas no tipo de evento
            switch (eventType) {
                case "CARD_CREATED" -> handleCardCreated(card);
                case "CARD_UPDATED" -> handleCardUpdated(card);
                case "CARD_DELETED" -> handleCardDeleted(card);
                case "CARD_MOVED" -> handleCardMoved(card);
                case "TAG_ASSIGNED" -> handleTagAssigned(card);
                case "TAG_REMOVED" -> handleTagRemoved(card);
                default -> System.out.println("[Observer] Evento não reconhecido: " + eventType);
            }
        }
    }

    private void handleCardCreated(Card card) {
        System.out.println("[Metrics] Card criado: " + card.getTitle() + " na coluna " + card.getColumn().getName());
        // Implementar lógica para atualizar métricas de criação
    }

    private void handleCardUpdated(Card card) {
        System.out.println("[Metrics] Card atualizado: " + card.getTitle());
        // Implementar lógica para atualizar métricas de edição
    }

    private void handleCardDeleted(Card card) {
        System.out.println("[Metrics] Card deletado: " + card.getTitle());
        // Implementar lógica para atualizar métricas de exclusão
    }

    private void handleCardMoved(Card card) {
        System.out.println("[Metrics] Card movido: " + card.getTitle() + " para " + card.getColumn().getName());
        // Implementar lógica para rastrear movimentação entre colunas
    }

    private void handleTagAssigned(Card card) {
        System.out.println("[Metrics] Tag atribuída ao card: " + card.getTitle());
        // Implementar lógica para rastrear uso de tags
    }

    private void handleTagRemoved(Card card) {
        System.out.println("[Metrics] Tag removida do card: " + card.getTitle());
        // Implementar lógica para rastrear remoção de tags
    }
}

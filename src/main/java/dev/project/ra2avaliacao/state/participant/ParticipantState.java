package dev.project.ra2avaliacao.state.participant;

import dev.project.ra2avaliacao.models.ProjectParticipant;
import dev.project.ra2avaliacao.models.ParticipantRole;

public interface ParticipantState {
    void promote(ProjectParticipant participant);
    void demote(ProjectParticipant participant);
    ParticipantRole getRole();
}

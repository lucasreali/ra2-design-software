package dev.project.ra2avaliacao.state.participant;

import dev.project.ra2avaliacao.models.ProjectParticipant;
import dev.project.ra2avaliacao.models.ParticipantRole;

public class AdminState implements ParticipantState {
    @Override
    public void promote(ProjectParticipant participant) {
        throw new UnsupportedOperationException("Admin cannot be promoted to Creator");
    }

    @Override
    public void demote(ProjectParticipant participant) {
        participant.setRole(ParticipantRole.MEMBER);
    }

    @Override
    public ParticipantRole getRole() {
        return ParticipantRole.ADMIN;
    }
}

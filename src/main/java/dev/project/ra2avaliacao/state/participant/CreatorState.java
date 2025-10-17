package dev.project.ra2avaliacao.state.participant;

import dev.project.ra2avaliacao.models.ProjectParticipant;
import dev.project.ra2avaliacao.models.ParticipantRole;

public class CreatorState implements ParticipantState {
    @Override
    public void promote(ProjectParticipant participant) {
        throw new UnsupportedOperationException("Creator cannot be promoted");
    }

    @Override
    public void demote(ProjectParticipant participant) {
        throw new UnsupportedOperationException("Creator cannot be demoted");
    }

    @Override
    public ParticipantRole getRole() {
        return ParticipantRole.CREATOR;
    }
}

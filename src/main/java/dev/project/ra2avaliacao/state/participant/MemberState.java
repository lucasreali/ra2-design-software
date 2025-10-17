package dev.project.ra2avaliacao.state.participant;

import dev.project.ra2avaliacao.models.ProjectParticipant;
import dev.project.ra2avaliacao.models.ParticipantRole;

public class MemberState implements ParticipantState {
    @Override
    public void promote(ProjectParticipant participant) {
        participant.setRole(ParticipantRole.ADMIN);
    }

    @Override
    public void demote(ProjectParticipant participant) {
        throw new UnsupportedOperationException("Member cannot be demoted");
    }

    @Override
    public ParticipantRole getRole() {
        return ParticipantRole.MEMBER;
    }
}

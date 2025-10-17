package dev.project.ra2avaliacao.state.participant;

import dev.project.ra2avaliacao.models.ProjectParticipant;
import dev.project.ra2avaliacao.models.ParticipantRole;

public interface ParticipantState {
    void promote(ProjectParticipant participant); // subir nível (ex: Member → Admin)
    void demote(ProjectParticipant participant);  // descer nível (ex: Admin → Member)
    ParticipantRole getRole();                    // retorna o tipo de role
}

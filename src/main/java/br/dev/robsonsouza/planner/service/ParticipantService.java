package br.dev.robsonsouza.planner.service;

import java.util.List;
import java.util.UUID;

import br.dev.robsonsouza.planner.dto.ParticipantCreateResponse;
import br.dev.robsonsouza.planner.dto.ParticipantData;
import br.dev.robsonsouza.planner.model.Participant;
import br.dev.robsonsouza.planner.model.Trip;
import br.dev.robsonsouza.planner.repository.ParticipantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ParticipantService {
    
    private final ParticipantRepository repository;
    
    public void registerParticipantsToEvent(List<String> participantsToInvite, Trip trip) {
        List<Participant> participants =
                participantsToInvite.stream().map(email -> new Participant(email, trip)).toList();
        
        this.repository.saveAllAndFlush(participants);
        participants.forEach(participant -> System.out.println(participant.getId()));
    }
    
    public ParticipantCreateResponse registerParticipantToTrip(String email, Trip trip) {
        
        Participant participant = new Participant(email, trip);
        this.repository.saveAndFlush(participant);
        return new ParticipantCreateResponse(participant.getId());
    }
    
    public void triggerConfirmationEmailToParticipants(UUID tripId) {}
    
    public void triggerConfirmationEmailToParticipant(UUID tripId, String email) {
    }
    
    public List<ParticipantData> getAllParticipantsFromTrip(UUID id) {
        return repository.findParticipantsByTripId(id)
                .stream()
                .map(participant -> new ParticipantData(participant.getId(), participant.getName(),
                                                        participant.getEmail(),
                                                        participant.getIsConfirmed()))
                .toList();
    }
}

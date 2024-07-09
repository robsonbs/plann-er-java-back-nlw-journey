package br.dev.robsonsouza.planner.repository;

import java.util.List;
import java.util.UUID;

import br.dev.robsonsouza.planner.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, UUID> {
    
    List<Participant> findParticipantsByTripId(UUID tripId);
}

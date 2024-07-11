package br.dev.robsonsouza.planner.controller;

import java.util.Optional;
import java.util.UUID;

import br.dev.robsonsouza.planner.dto.ParticipantRequestPayload;
import br.dev.robsonsouza.planner.model.Participant;
import br.dev.robsonsouza.planner.repository.ParticipantRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/participants")
@AllArgsConstructor
public class ParticipantController {
    
    private ParticipantRepository repository;
    
    @PostMapping("/{id}/confirm")
    public ResponseEntity<Participant> confirmPatricipant(@PathVariable UUID id,
                                                          @RequestBody ParticipantRequestPayload payload) {
        Optional<Participant> participant = this.repository.findById(id);
        if (participant.isPresent()) {
            Participant rawParticipant = participant.get();
            rawParticipant.setIsConfirmed(true);
            rawParticipant.setName(payload.name());
            
            repository.save(rawParticipant);
            return ResponseEntity.ok(rawParticipant);
        }
        return ResponseEntity.notFound().build();
    }
}

package br.dev.robsonsouza.planner.controller;

import br.dev.robsonsouza.planner.dto.TripCreateResponse;
import br.dev.robsonsouza.planner.dto.TripRequestPayload;
import br.dev.robsonsouza.planner.model.Trip;
import br.dev.robsonsouza.planner.repository.TripRepository;
import br.dev.robsonsouza.planner.service.ParticipantService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/trips")
@AllArgsConstructor
public class TripController {
    
    private ParticipantService participantService;
    private TripRepository repository;
    
    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload) {
        Trip newTrip = new Trip(payload);
        this.repository.save(newTrip);
        this.participantService.registerParticipantsToEvent(payload.emails_to_invite(),
                                                            newTrip.getId());
        return ResponseEntity.ok(new TripCreateResponse(newTrip.getId()));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) {
        Optional<Trip> trip = this.repository.findById(id);
        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

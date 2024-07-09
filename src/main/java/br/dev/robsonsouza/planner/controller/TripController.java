package br.dev.robsonsouza.planner.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.dev.robsonsouza.planner.dto.ParticipantCreateResponse;
import br.dev.robsonsouza.planner.dto.ParticipantData;
import br.dev.robsonsouza.planner.dto.ParticipantRequestPayload;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        this.participantService.registerParticipantsToEvent(payload.emailsToInvite(), newTrip);
        return ResponseEntity.ok(new TripCreateResponse(newTrip.getId()));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) {
        Optional<Trip> trip = this.repository.findById(id);
        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id,
                                           @RequestBody TripRequestPayload payload) {
        Optional<Trip> trip = this.repository.findById(id);
        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setEndsAt(
                    LocalDateTime.parse(payload.endsAt(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setStartsAt(
                    LocalDateTime.parse(payload.startsAt(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setDestination(payload.destination());
            repository.save(rawTrip);
            return ResponseEntity.ok(rawTrip);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) {
        Optional<Trip> trip = this.repository.findById(id);
        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setIsConfimed(true);
            repository.save(rawTrip);
            participantService.triggerConfirmationEmailToParticipants(id);
            return ResponseEntity.ok(rawTrip);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id,
                                                                       @RequestBody ParticipantRequestPayload payload) {
        Optional<Trip> trip = repository.findById(id);
        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            
            ParticipantCreateResponse participantCreateResponse =
                    this.participantService.registerParticipantToEvent(payload.email(), rawTrip);
            if (Boolean.TRUE.equals(rawTrip.getIsConfimed())) {
                this.participantService.triggerConfirmationEmailToParticipant(rawTrip.getId(),
                                                                              payload.email());
            }
            return ResponseEntity.ok(participantCreateResponse);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> getAllParticipants(@PathVariable UUID id) {
        List<ParticipantData> participantList = participantService.getAllParticipantsFromTrip(id);
        
        return ResponseEntity.ok(participantList);
    }
}

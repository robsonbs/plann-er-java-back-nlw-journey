package br.dev.robsonsouza.planner.controller;

import java.util.List;
import java.util.UUID;

import br.dev.robsonsouza.planner.dto.ActivityCreateResponse;
import br.dev.robsonsouza.planner.dto.ActivityData;
import br.dev.robsonsouza.planner.dto.ActivityRequestPayload;
import br.dev.robsonsouza.planner.dto.LinkCreateResponse;
import br.dev.robsonsouza.planner.dto.LinkData;
import br.dev.robsonsouza.planner.dto.LinkRequestPayload;
import br.dev.robsonsouza.planner.dto.ParticipantCreateResponse;
import br.dev.robsonsouza.planner.dto.ParticipantData;
import br.dev.robsonsouza.planner.dto.ParticipantRequestPayload;
import br.dev.robsonsouza.planner.dto.TripCreateResponse;
import br.dev.robsonsouza.planner.dto.TripRequestPayload;
import br.dev.robsonsouza.planner.dto.TripResponse;
import br.dev.robsonsouza.planner.exceptions.ErrorDateException;
import br.dev.robsonsouza.planner.service.TripService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class TripController {
    
    private final TripService tripService;
    
    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(
            @RequestBody TripRequestPayload playload) throws ErrorDateException {
        TripCreateResponse newTrip = this.tripService.saveTrip(playload);
        return ResponseEntity.ok(newTrip);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TripResponse> getTripDetails(@PathVariable UUID id) {
        try {
            TripResponse response = tripService.getTripDetails(id);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {return ResponseEntity.notFound().build();}
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TripResponse> updateTrip(@PathVariable UUID id,
                                                   @RequestBody TripRequestPayload payload) throws
            ErrorDateException {
        TripResponse tripResponse = tripService.updateTrip(id, payload);
        return ResponseEntity.ok(tripResponse);
    }
    
    @GetMapping("/{id}/confirm")
    public ResponseEntity<TripResponse> confirmTrip(@PathVariable UUID id) {
        try {
            TripResponse tripResponse = tripService.confirmTrip(id);
            return ResponseEntity.ok(tripResponse);
        } catch (Exception ex) {return ResponseEntity.notFound().build();}
    }
    
    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityCreateResponse> registerActivity(@PathVariable UUID id,
                                                                   @RequestBody ActivityRequestPayload payload) throws
            ErrorDateException {
        ActivityCreateResponse activityResponse = tripService.saveActivity(id, payload);
        return ResponseEntity.ok(activityResponse);
    }
    
    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityData>> getAllActivities(@PathVariable UUID id) {
        List<ActivityData> activities = this.tripService.getAllActivitiesTrip(id);
        return ResponseEntity.ok(activities);
    }
    
    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id,
                                                                       @RequestBody ParticipantRequestPayload payload) {
        ParticipantCreateResponse participantCreateResponse =
                this.tripService.inviteParticipant(id, payload);
        return ResponseEntity.ok(participantCreateResponse);
    }
    
    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> getAllParticipants(@PathVariable UUID id) {
        List<ParticipantData> participants = this.tripService.getAllParticipantsTrip(id);
        return ResponseEntity.ok(participants);
    }
    
    @PostMapping("/{id}/links")
    public ResponseEntity<LinkCreateResponse> registerLink(@PathVariable UUID id,
                                                           @RequestBody LinkRequestPayload payload) {
        LinkCreateResponse linkResponse = this.tripService.saveLink(id, payload);
        return ResponseEntity.ok(linkResponse);
    }
    
    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkData>> getAllLinks(@PathVariable UUID id) {
        List<LinkData> links = this.tripService.getAllLinksTrip(id);
        return ResponseEntity.ok(links);
    }
}

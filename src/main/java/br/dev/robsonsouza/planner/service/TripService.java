package br.dev.robsonsouza.planner.service;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import br.dev.robsonsouza.planner.exceptions.ResourceNotFoundException;
import br.dev.robsonsouza.planner.model.Trip;
import br.dev.robsonsouza.planner.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TripService {
    
    private final TripRepository tripRepository;
    private final ParticipantService participantService;
    private final ActivityService activityService;
    private final LinkService linkService;
    
    public TripCreateResponse saveTrip(TripRequestPayload payload) throws ErrorDateException {
        
        validaFormatoData(payload.startsAt());
        validaFormatoData(payload.endsAt());
        
        Trip newTrip = new Trip(payload);
        
        if (newTrip.getStartsAt().isAfter(newTrip.getEndsAt())) {
            throw new ErrorDateException("A data de início não pode ser maior que a data de fim.");
        }
        
        this.tripRepository.save(newTrip);
        this.participantService.registerParticipantsToEvent(payload.emailsToInvite(), newTrip);
        
        return new TripCreateResponse(newTrip.getId());
    }
    
    public TripResponse getTripDetails(UUID tripId) {
        return this.tripRepository.findById(tripId)
                .map(this::to)
                .orElseThrow(ResourceNotFoundException::new);
    }
    
    public TripResponse updateTrip(UUID tripId, TripRequestPayload payload) throws
            ErrorDateException {
        
        validaFormatoData(payload.startsAt());
        validaFormatoData(payload.endsAt());
        
        Trip trip = this.tripRepository.findById(tripId)
                .orElseThrow(ResourceNotFoundException::new);
        updateDataTrip(trip, payload);
        
        if (trip.getStartsAt().isAfter(trip.getEndsAt())) {
            throw new ErrorDateException("A data de início não pode ser maior que a data de fim.");
        }
        
        this.tripRepository.save(trip);
        return to(trip);
    }
    
    public TripResponse confirmTrip(UUID tripId) throws ResourceNotFoundException {
        Trip trip = this.tripRepository.findById(tripId)
                .orElseThrow(ResourceNotFoundException::new);
        trip.setConfirmed(true);
        this.tripRepository.save(trip);
        
        return to(trip);
    }
    
    public ActivityCreateResponse saveActivity(UUID tripId, ActivityRequestPayload payload) throws
            ErrorDateException {
        validaFormatoData(payload.occursAt());
        Trip trip = this.tripRepository.findById(tripId)
                .orElseThrow(ResourceNotFoundException::new);
        verificaDataActivity(payload.occursAt(), trip);
        return this.activityService.registerActivity(payload, trip);
    }
    
    public List<ActivityData> getAllActivitiesTrip(UUID tripId) {
        Trip trip = this.tripRepository.findById(tripId)
                .orElseThrow(ResourceNotFoundException::new);
        return this.activityService.getAllActivitiesFromTrip(trip.getId());
    }
    
    public ParticipantCreateResponse inviteParticipant(UUID tripId,
                                                       ParticipantRequestPayload participantRequestPayload) {
        Trip trip = this.tripRepository.findById(tripId)
                .orElseThrow(ResourceNotFoundException::new);
        
        ParticipantCreateResponse participantCreateResponse =
                this.participantService.registerParticipantToTrip(participantRequestPayload.email(),
                                                                  trip);
        
        if (Boolean.TRUE.equals(trip.isConfirmed())) {
            this.participantService.triggerConfirmationEmailToParticipant(trip.getId(),
                                                                          participantRequestPayload.email());
        }
        
        return participantCreateResponse;
    }
    
    public List<ParticipantData> getAllParticipantsTrip(UUID tripId) {
        Trip trip = this.tripRepository.findById(tripId)
                .orElseThrow(ResourceNotFoundException::new);
        
        return this.participantService.getAllParticipantsFromTrip(trip.getId());
    }
    
    public LinkCreateResponse saveLink(UUID tripId, LinkRequestPayload payload) {
        Trip trip = this.tripRepository.findById(tripId)
                .orElseThrow(ResourceNotFoundException::new);
        
        return this.linkService.registerLink(payload, trip);
    }
    
    public List<LinkData> getAllLinksTrip(UUID tripId) {
        
        Trip trip = this.tripRepository.findById(tripId)
                .orElseThrow(ResourceNotFoundException::new);
        
        return this.linkService.getAllLinksFromTrip(trip.getId());
    }
    
    private void validaFormatoData(String data) throws ErrorDateException {
        try {
            LocalDateTime.parse(data, DateTimeFormatter.ISO_DATE_TIME);
        } catch (DateTimeException exception) {
            throw new ErrorDateException(
                    "Formato de data invalido. Use o formato ISO_DATE_TIME. Ex: 2021-08-01T10:00:00");
        }
    }
    
    private void updateDataTrip(Trip trip, TripRequestPayload payload) {
        trip.setEndsAt(LocalDateTime.parse(payload.endsAt(), DateTimeFormatter.ISO_DATE_TIME));
        trip.setStartsAt(LocalDateTime.parse(payload.startsAt(), DateTimeFormatter.ISO_DATE_TIME));
        trip.setDestination(payload.destination());
    }
    
    private void verificaDataActivity(String occursAt, Trip trip) throws ErrorDateException {
        
        LocalDateTime data = LocalDateTime.parse(occursAt, DateTimeFormatter.ISO_DATE_TIME);
        
        if (data.isBefore(trip.getStartsAt()) || data.isAfter(trip.getEndsAt())) {
            throw new ErrorDateException(
                    "A data da atividade deve estar entre a data de início e fim da viagem.");
        }
    }
    
    private TripResponse to(Trip trip) {
        return new TripResponse(trip.getId(), trip.getDestination(),
                                trip.getStartsAt().format(DateTimeFormatter.ISO_DATE_TIME),
                                trip.getEndsAt().format(DateTimeFormatter.ISO_DATE_TIME),
                                trip.getOwnerName(), trip.getOwnerEmail(), trip.isConfirmed());
    }
}
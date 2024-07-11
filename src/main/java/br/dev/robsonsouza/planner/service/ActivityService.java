package br.dev.robsonsouza.planner.service;

import java.util.List;
import java.util.UUID;

import br.dev.robsonsouza.planner.dto.ActivityCreateResponse;
import br.dev.robsonsouza.planner.dto.ActivityData;
import br.dev.robsonsouza.planner.dto.ActivityRequestPayload;
import br.dev.robsonsouza.planner.model.Activity;
import br.dev.robsonsouza.planner.model.Trip;
import br.dev.robsonsouza.planner.repository.ActivityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ActivityService {
    
    private ActivityRepository repository;
    
    public ActivityCreateResponse registerActivity(ActivityRequestPayload payload, Trip trip) {
        Activity newActivity = new Activity(payload, trip);
        repository.saveAndFlush(newActivity);
        
        return new ActivityCreateResponse(newActivity.getId());
    }
    
    public List<ActivityData> getAllActivitiesFromTrip(UUID id) {
        return repository.findActivitiesByTripId(id)
                .stream()
                .map(activity -> new ActivityData(activity.getId(), activity.getTitle(),
                                                  activity.getOccursAt(), activity.getIsDone()))
                .toList();
    }
}

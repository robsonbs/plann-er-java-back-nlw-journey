package br.dev.robsonsouza.planner.repository;

import java.util.List;
import java.util.UUID;

import br.dev.robsonsouza.planner.model.Activity;
import br.dev.robsonsouza.planner.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    
    List<Activity> findActivitiesByTripId(UUID tripId);
}

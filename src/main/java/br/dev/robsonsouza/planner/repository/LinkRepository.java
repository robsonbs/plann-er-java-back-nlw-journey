package br.dev.robsonsouza.planner.repository;

import java.util.List;
import java.util.UUID;

import br.dev.robsonsouza.planner.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<Link, UUID> {
    
    List<Link> findLinksByTripId(UUID tripId);
}

package br.dev.robsonsouza.planner.repository;

import br.dev.robsonsouza.planner.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TripRepository extends JpaRepository<Trip, UUID> {}

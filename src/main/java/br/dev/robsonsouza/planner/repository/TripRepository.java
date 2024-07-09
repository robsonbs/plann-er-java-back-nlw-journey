package br.dev.robsonsouza.planner.repository;

import java.util.UUID;

import br.dev.robsonsouza.planner.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, UUID> {}

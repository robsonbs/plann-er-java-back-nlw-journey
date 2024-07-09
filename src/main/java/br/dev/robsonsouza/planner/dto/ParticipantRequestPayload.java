package br.dev.robsonsouza.planner.dto;

import br.dev.robsonsouza.planner.model.Trip;

public record ParticipantRequestPayload(String name, String email, Trip trip) {}

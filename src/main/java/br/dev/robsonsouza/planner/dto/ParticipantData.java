package br.dev.robsonsouza.planner.dto;

import java.util.UUID;

public record ParticipantData(UUID id, String name, String email, boolean isConfirmad) {}

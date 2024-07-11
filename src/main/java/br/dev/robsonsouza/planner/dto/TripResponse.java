package br.dev.robsonsouza.planner.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TripResponse(UUID id, String destination, @JsonProperty("starts_at") String startsAt,
                           @JsonProperty("ends_at") String endsAt,
                           @JsonProperty("owner_email") String ownerEmail,
                           @JsonProperty("owner_name") String ownerName,
                           @JsonProperty("is_confirmed") boolean isConfirmed) {}

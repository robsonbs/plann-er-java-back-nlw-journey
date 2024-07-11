package br.dev.robsonsouza.planner.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ActivityData(UUID id, String title, @JsonProperty("occurs_at") LocalDateTime occursAt,
                           @JsonProperty("is_done") boolean isDone) {}

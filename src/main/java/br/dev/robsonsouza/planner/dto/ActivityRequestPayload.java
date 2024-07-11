package br.dev.robsonsouza.planner.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ActivityRequestPayload(String title, @JsonProperty("occurs_at") String occursAt) {}

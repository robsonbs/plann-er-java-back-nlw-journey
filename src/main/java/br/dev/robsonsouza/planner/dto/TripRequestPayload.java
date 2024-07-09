package br.dev.robsonsouza.planner.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TripRequestPayload(String destination, @JsonProperty("starts_at") String startsAt,
                                 @JsonProperty("ends_at") String endsAt,
                                 @JsonProperty("emails_to_invite") List<String> emailsToInvite,
                                 @JsonProperty("owner_email") String ownerEmail,
                                 @JsonProperty("owner_name") String ownerName) {}

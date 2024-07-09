package br.dev.robsonsouza.planner.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import br.dev.robsonsouza.planner.dto.TripRequestPayload;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "trips")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Trip {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String destination;
    @Column(nullable = false, name = "owner_name")
    @JsonProperty("owner_name")
    private String ownerName;
    @Column(nullable = false, name = "owner_email")
    @JsonProperty("owner_email")
    private String ownerEmail;
    @Column(nullable = false, name = "startsAt")
    @JsonProperty("starts_at")
    private LocalDateTime startsAt;
    @JsonProperty("ends_at")
    @Column(nullable = false, name = "ends_at")
    private LocalDateTime endsAt;
    @JsonProperty("is_confirmed")
    @Column(nullable = false, name = "is_confirmed")
    private Boolean isConfimed;
    
    public Trip(TripRequestPayload data) {
        this.destination = data.destination();
        this.ownerName = data.ownerName();
        this.ownerEmail = data.ownerEmail();
        this.startsAt = LocalDateTime.parse(data.startsAt(), DateTimeFormatter.ISO_DATE_TIME);
        this.endsAt = LocalDateTime.parse(data.endsAt(), DateTimeFormatter.ISO_DATE_TIME);
        this.isConfimed = false;
    }
}

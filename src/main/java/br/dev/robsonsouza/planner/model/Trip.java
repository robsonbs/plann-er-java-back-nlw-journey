package br.dev.robsonsouza.planner.model;

import br.dev.robsonsouza.planner.dto.TripRequestPayload;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

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
    private String ownerName;
    @Column(nullable = false, name = "owner_email")
    private String ownerEmail;
    @Column(nullable = false, name = "starts_at")
    private LocalDateTime startsAt;
    @Column(nullable = false, name = "ends_at")
    private LocalDateTime endsAt;
    @Column(nullable = false, name = "is_confirmed")
    private Boolean isConfimed;
    
    public Trip(TripRequestPayload data) {
        this.destination = data.destination();
        this.ownerName = data.owner_name();
        this.ownerEmail = data.owner_email();
        this.startsAt = LocalDateTime.parse(data.starts_at(), DateTimeFormatter.ISO_DATE_TIME);
        this.endsAt = LocalDateTime.parse(data.ends_at(), DateTimeFormatter.ISO_DATE_TIME);
        this.isConfimed = false;
    }
}

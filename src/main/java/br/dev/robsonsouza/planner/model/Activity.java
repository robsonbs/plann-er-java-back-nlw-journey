package br.dev.robsonsouza.planner.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import br.dev.robsonsouza.planner.dto.ActivityRequestPayload;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "activities")
public class Activity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String title;
    @Column(name = "occurs_at", nullable = false)
    private LocalDateTime occursAt;
    @Column(name = "is_done", nullable = false)
    private Boolean isDone;
    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;
    @Column(name="trip_id", insertable = false, updatable = false)
    @JsonProperty("trip_id")
    private UUID tripId;
    
    public Activity(ActivityRequestPayload data, Trip trip) {
        this.trip = trip;
        this.title = data.title();
        this.occursAt = LocalDateTime.parse(data.occursAt(), DateTimeFormatter.ISO_DATE_TIME);
        this.isDone = false;
    }
}

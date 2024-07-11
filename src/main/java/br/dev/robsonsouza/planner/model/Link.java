package br.dev.robsonsouza.planner.model;

import java.util.UUID;

import br.dev.robsonsouza.planner.dto.LinkRequestPayload;
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
@Entity(name = "links")
public class Link {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String url;
    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;
    @Column(name="trip_id", insertable = false, updatable = false)
    @JsonProperty("trip_id")
    private UUID tripId;
    
    public Link(LinkRequestPayload data, Trip trip) {
        this.trip = trip;
        this.title = data.title();
        this.url = data.url();
    }
}

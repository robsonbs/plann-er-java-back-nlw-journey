package br.dev.robsonsouza.planner.model;

import java.util.UUID;

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

@Entity(name = "participants")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Participant {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false, name = "is_confirmed")
    private Boolean isConfirmed;
    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;
    @Column(insertable = false, updatable = false)
    private UUID trip_id;
    
    public Participant(String email, Trip trip) {
        this.name = "";
        this.isConfirmed = false;
        this.email = email;
        this.trip = trip;
    }
}

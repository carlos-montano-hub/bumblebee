package com.beehive.beehive_nest.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Measure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime time;
    @ManyToOne
    @JoinColumn(name = "beehive_id", nullable = false)
    @JsonBackReference
    private Beehive beehive;
    private double temperature;
    private double humidity;
    private double weight;
    private String audioRecordingUrl;
}

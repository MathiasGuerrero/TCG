package com.example.torneos.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "torneos")
public class Torneos extends RepresentationModel<Torneos> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreTorneo;

    private int participantes;

    private String premio;

    private int duracion;





}

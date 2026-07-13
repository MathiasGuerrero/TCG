package com.example.torneos.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inscripciones")
public class Inscripcion extends RepresentationModel<Inscripcion> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long torneoId;
    private Long usuarioId;
    private String usernameUsuario;

}

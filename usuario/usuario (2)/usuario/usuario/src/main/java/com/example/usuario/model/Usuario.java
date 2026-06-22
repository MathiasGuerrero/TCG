package com.example.usuario.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Usuario extends RepresentationModel<Usuario> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username es obligatorio")
    @Size(min = 2, max = 25, message = "El username debe tener entre 2 y 25 caracteres")
    private String username;

    @Email(message = "Debe ser un correo válido")
    @NotBlank(message = "Correo es obligatorio")
    private String correo;



}

package com.example.pedidos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva extends RepresentationModel<Reserva> {
    private Long id;
    private String producto;
    private int cantidad;
    private String tipoPago;
    private int monto;
}

package com.example.pedidos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {
    private Long id;
    private String producto;
    private int cantidad;
    private String tipoPago;
    private int monto;
}

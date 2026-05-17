package com.example.pedidos.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {
    private Long id;
    private BigDecimal precio;
    private String nombre;
    private Integer stock;
}

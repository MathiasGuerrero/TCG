package com.example.pedidos.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedido{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productoId;
    private String nombreProducto;
    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    @JsonBackReference
    private Pedido pedido;
}

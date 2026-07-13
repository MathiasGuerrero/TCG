package com.example.wishlist.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto extends RepresentationModel<Producto> {

    private Long id;
    private BigDecimal precio;
    private String nombre;
    private Integer stock;
}

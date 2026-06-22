package com.example.wishlist.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistRequest extends RepresentationModel<WishlistRequest> {

    private Long usuarioId;
    private Long productoId;
}

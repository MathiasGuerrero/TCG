package com.example.wishlist.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.example.wishlist.dto.WishlistRequest;
import com.example.wishlist.model.Wishlist;
import com.example.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService service;

    @PostMapping
    public ResponseEntity<EntityModel<Wishlist>> save(@RequestBody WishlistRequest request) {
        Wishlist item = service.save(request);

        EntityModel<Wishlist> resource = EntityModel.of(item,
                linkTo(methodOn(WishlistController.class).getByUsuarioId(item.getUsuarioId())).withRel("wishlistUsuario"),
                linkTo(methodOn(WishlistController.class).eliminar(item.getUsuarioId(), item.getProductoId())).withRel("eliminar")
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<CollectionModel<EntityModel<Wishlist>>> getByUsuarioId(@PathVariable Long usuarioId) {
        List<EntityModel<Wishlist>> lista = service.getByUsuario(usuarioId)
                .stream()
                .map(item -> EntityModel.of(item,
                        linkTo(methodOn(WishlistController.class).getByUsuarioId(usuarioId)).withSelfRel(),
                        linkTo(methodOn(WishlistController.class).eliminar(item.getUsuarioId(), item.getProductoId())).withRel("eliminar")
                ))
                .toList();

        CollectionModel<EntityModel<Wishlist>> collection = CollectionModel.of(lista,
                linkTo(methodOn(WishlistController.class).getByUsuarioId(usuarioId)).withSelfRel()
        );

        return ResponseEntity.status(HttpStatus.OK).body(collection);
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminar(
            @RequestParam Long usuarioId,
            @RequestParam Long productoId) {
        service.eliminar(usuarioId, productoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
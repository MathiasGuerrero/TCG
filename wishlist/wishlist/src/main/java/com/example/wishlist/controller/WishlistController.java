package com.example.wishlist.controller;


import com.example.wishlist.dto.WishlistRequest;
import com.example.wishlist.model.Wishlist;
import com.example.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Wishlist> save(@RequestBody WishlistRequest request) {
        Wishlist item = service.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Wishlist>> getByUsuarioId(@PathVariable Long usuarioId) {
        List<Wishlist> lista = service.getByUsuario(usuarioId);
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminar(
            @RequestParam Long usuarioId,
            @RequestParam Long productoId) {
        service.eliminar(usuarioId, productoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

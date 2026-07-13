package com.example.FireTCG_Store.controller;


import com.example.FireTCG_Store.model.Carta;
import com.example.FireTCG_Store.service.CartaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/cartas")
@RestController
public class CartaController {

    @Autowired
    private CartaService service;

    @PostMapping
    public ResponseEntity<Carta> crear(@Valid @RequestBody Carta nuevaCarta) {
        Carta carta = service.crear(nuevaCarta);
        return ResponseEntity.status(HttpStatus.CREATED).body(carta);
    }

    @GetMapping
    public ResponseEntity<List<Carta>> getCartas() {
        List<Carta> cartas = service.getCartas();
        return ResponseEntity.status(HttpStatus.OK).body(cartas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carta> findById(@PathVariable Long id) {
        Carta carta = service.filtrarById(id);
        return ResponseEntity.status(HttpStatus.OK).body(carta);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<Carta>> findByExpansion(@PathVariable String nombre) {
        List<Carta> cartas = service.filtrarByNombre(nombre);
        return ResponseEntity.status(HttpStatus.OK).body(cartas);
    }

    @GetMapping("/tcg/{tcg}")
    public ResponseEntity<List<Carta>> findByTcg(@PathVariable String tcg) {
        List<Carta> cartas = service.filtrarByTcg(tcg);
        return ResponseEntity.status(HttpStatus.OK).body(cartas);
    }

    @GetMapping("/anio/{anio}")
    public ResponseEntity<List<Carta>> findByAnio(@PathVariable Integer anio) {
        List<Carta> cartas = service.filtrarByAnio(anio);
        return ResponseEntity.status(HttpStatus.OK).body(cartas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
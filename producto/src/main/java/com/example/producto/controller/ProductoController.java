package com.example.producto.controller;


import com.example.producto.model.Producto;
import com.example.producto.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    @Autowired
    private ProductoService service;

    @GetMapping
    public ResponseEntity<List<Producto>> getProductos() {
        List<Producto> productos = service.getProductos();
        return ResponseEntity.status(HttpStatus.OK).body(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable Long id) {
        Producto producto = service.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(producto);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Producto> getByNombre(@PathVariable String nombre) {
        Producto producto = service.findByNombre(nombre);
        return ResponseEntity.status(HttpStatus.OK).body(producto);
    }

    @PostMapping
    public ResponseEntity<Producto> saveProducto(@Valid @RequestBody Producto nuevoProducto) {
        Producto producto = service.saveProducto(nuevoProducto);
        return ResponseEntity.status(HttpStatus.CREATED).body(producto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/precio")
    public ResponseEntity<List<Producto>> findByPrecioBetween(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {

        List<Producto> productos = service.findByPrecioBetween(min, max);
        return ResponseEntity.status(HttpStatus.OK).body(productos);
    }
}
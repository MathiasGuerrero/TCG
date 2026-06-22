package com.example.producto.controller;


import com.example.producto.model.Producto;
import com.example.producto.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    @Autowired
    private ProductoService service;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> getProductos() {
        List<EntityModel<Producto>> productos = service.getProductos()
                .stream()
                .map(producto -> EntityModel.of(producto,
                        linkTo(methodOn(ProductoController.class).getById(producto.getId())).withSelfRel(),
                        linkTo(methodOn(ProductoController.class).getProductos()).withRel("todos"),
                        linkTo(methodOn(ProductoController.class).deleteProducto(producto.getId())).withRel("eliminar")
                ))
                .toList();

        CollectionModel<EntityModel<Producto>> collection = CollectionModel.of(productos,
                linkTo(methodOn(ProductoController.class).getProductos()).withSelfRel()
        );

        return ResponseEntity.status(HttpStatus.OK).body(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> getById(@PathVariable Long id) {
        Producto producto = service.findById(id);

        EntityModel<Producto> resource = EntityModel.of(producto,
                linkTo(methodOn(ProductoController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(ProductoController.class).getProductos()).withRel("todos"),
                linkTo(methodOn(ProductoController.class).deleteProducto(id)).withRel("eliminar")
        );

        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<EntityModel<Producto>> getByNombre(@PathVariable String nombre) {
        Producto producto = service.findByNombre(nombre);

        EntityModel<Producto> resource = EntityModel.of(producto,
                linkTo(methodOn(ProductoController.class).getByNombre(nombre)).withSelfRel(),
                linkTo(methodOn(ProductoController.class).getById(producto.getId())).withRel("detalle"),
                linkTo(methodOn(ProductoController.class).getProductos()).withRel("todos")
        );

        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }

    @PostMapping
    public ResponseEntity<EntityModel<Producto>> saveProducto(@Valid @RequestBody Producto nuevoProducto) {
        Producto producto = service.saveProducto(nuevoProducto);

        EntityModel<Producto> resource = EntityModel.of(producto,
                linkTo(methodOn(ProductoController.class).getById(producto.getId())).withSelfRel(),
                linkTo(methodOn(ProductoController.class).getProductos()).withRel("todos"),
                linkTo(methodOn(ProductoController.class).deleteProducto(producto.getId())).withRel("eliminar")
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/precio")
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> findByPrecioBetween(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {

        List<EntityModel<Producto>> productos = service.findByPrecioBetween(min, max)
                .stream()
                .map(producto -> EntityModel.of(producto,
                        linkTo(methodOn(ProductoController.class).getById(producto.getId())).withSelfRel(),
                        linkTo(methodOn(ProductoController.class).deleteProducto(producto.getId())).withRel("eliminar")
                ))
                .toList();

        CollectionModel<EntityModel<Producto>> collection = CollectionModel.of(productos,
                linkTo(methodOn(ProductoController.class).findByPrecioBetween(min, max)).withSelfRel(),
                linkTo(methodOn(ProductoController.class).getProductos()).withRel("todos")
        );

        return ResponseEntity.status(HttpStatus.OK).body(collection);
    }
}

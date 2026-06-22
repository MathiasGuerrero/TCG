package com.example.FireTCG_Store.controller;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.example.FireTCG_Store.model.Carta;
import com.example.FireTCG_Store.service.CartaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/v1/cartas")
@RestController
public class CartaController {

    @Autowired
    private CartaService service;

    @PostMapping
    public ResponseEntity<EntityModel<Carta>> crear(@Valid @RequestBody Carta nuevaCarta) {
        Carta carta = service.crear(nuevaCarta);

        EntityModel<Carta> resource = EntityModel.of(carta,
                linkTo(methodOn(CartaController.class).findById(carta.getId())).withSelfRel(),
                linkTo(methodOn(CartaController.class).getCartas()).withRel("todas"),
                linkTo(methodOn(CartaController.class).findByTcg(carta.getTcg())).withRel("porTcg"),
                linkTo(methodOn(CartaController.class).findByExpansion(carta.getNombre())).withRel("porNombre"),
                linkTo(methodOn(CartaController.class).findByAnio(carta.getAnio())).withRel("porAnio"),
                linkTo(methodOn(CartaController.class).deleteById(carta.getId())).withRel("eliminar")
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Carta>>> getCartas() {
        List<EntityModel<Carta>> cartas = service.getCartas()
                .stream()
                .map(carta -> EntityModel.of(carta,
                        linkTo(methodOn(CartaController.class).findById(carta.getId())).withSelfRel(),
                        linkTo(methodOn(CartaController.class).getCartas()).withRel("todas"),
                        linkTo(methodOn(CartaController.class).findByTcg(carta.getTcg())).withRel("porTcg"),
                        linkTo(methodOn(CartaController.class).findByExpansion(carta.getNombre())).withRel("porNombre"),
                        linkTo(methodOn(CartaController.class).findByAnio(carta.getAnio())).withRel("porAnio"),
                        linkTo(methodOn(CartaController.class).deleteById(carta.getId())).withRel("eliminar")
                ))
                .toList();

        CollectionModel<EntityModel<Carta>> collection = CollectionModel.of(cartas,
                linkTo(methodOn(CartaController.class).getCartas()).withSelfRel()
        );

        return ResponseEntity.status(HttpStatus.OK).body(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Carta>> findById(@PathVariable Long id) {
        Carta carta = service.filtrarById(id);

        EntityModel<Carta> resource = EntityModel.of(carta,
                linkTo(methodOn(CartaController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(CartaController.class).getCartas()).withRel("todas"),
                linkTo(methodOn(CartaController.class).findByTcg(carta.getTcg())).withRel("porTcg"),
                linkTo(methodOn(CartaController.class).findByExpansion(carta.getNombre())).withRel("porNombre"),
                linkTo(methodOn(CartaController.class).findByAnio(carta.getAnio())).withRel("porAnio"),
                linkTo(methodOn(CartaController.class).deleteById(id)).withRel("eliminar")
        );

        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<CollectionModel<EntityModel<Carta>>> findByExpansion(@PathVariable String nombre) {
        List<EntityModel<Carta>> cartas = service.filtrarByNombre(nombre)
                .stream()
                .map(carta -> EntityModel.of(carta,
                        linkTo(methodOn(CartaController.class).findById(carta.getId())).withSelfRel(),
                        linkTo(methodOn(CartaController.class).getCartas()).withRel("todas"),
                        linkTo(methodOn(CartaController.class).findByTcg(carta.getTcg())).withRel("porTcg"),
                        linkTo(methodOn(CartaController.class).findByAnio(carta.getAnio())).withRel("porAnio"),
                        linkTo(methodOn(CartaController.class).deleteById(carta.getId())).withRel("eliminar")
                ))
                .toList();

        CollectionModel<EntityModel<Carta>> collection = CollectionModel.of(cartas,
                linkTo(methodOn(CartaController.class).findByExpansion(nombre)).withSelfRel(),
                linkTo(methodOn(CartaController.class).getCartas()).withRel("todas")
        );

        return ResponseEntity.status(HttpStatus.OK).body(collection);
    }

    @GetMapping("/tcg/{tcg}")
    public ResponseEntity<CollectionModel<EntityModel<Carta>>> findByTcg(@PathVariable String tcg) {
        List<EntityModel<Carta>> cartas = service.filtrarByTcg(tcg)
                .stream()
                .map(carta -> EntityModel.of(carta,
                        linkTo(methodOn(CartaController.class).findById(carta.getId())).withSelfRel(),
                        linkTo(methodOn(CartaController.class).getCartas()).withRel("todas"),
                        linkTo(methodOn(CartaController.class).findByExpansion(carta.getNombre())).withRel("porNombre"),
                        linkTo(methodOn(CartaController.class).findByAnio(carta.getAnio())).withRel("porAnio"),
                        linkTo(methodOn(CartaController.class).deleteById(carta.getId())).withRel("eliminar")
                ))
                .toList();

        CollectionModel<EntityModel<Carta>> collection = CollectionModel.of(cartas,
                linkTo(methodOn(CartaController.class).findByTcg(tcg)).withSelfRel(),
                linkTo(methodOn(CartaController.class).getCartas()).withRel("todas")
        );

        return ResponseEntity.status(HttpStatus.OK).body(collection);
    }

    @GetMapping("/anio/{anio}")
    public ResponseEntity<CollectionModel<EntityModel<Carta>>> findByAnio(@PathVariable Integer anio) {
        List<EntityModel<Carta>> cartas = service.filtrarByAnio(anio)
                .stream()
                .map(carta -> EntityModel.of(carta,
                        linkTo(methodOn(CartaController.class).findById(carta.getId())).withSelfRel(),
                        linkTo(methodOn(CartaController.class).getCartas()).withRel("todas"),
                        linkTo(methodOn(CartaController.class).findByTcg(carta.getTcg())).withRel("porTcg"),
                        linkTo(methodOn(CartaController.class).findByExpansion(carta.getNombre())).withRel("porNombre"),
                        linkTo(methodOn(CartaController.class).deleteById(carta.getId())).withRel("eliminar")
                ))
                .toList();

        CollectionModel<EntityModel<Carta>> collection = CollectionModel.of(cartas,
                linkTo(methodOn(CartaController.class).findByAnio(anio)).withSelfRel(),
                linkTo(methodOn(CartaController.class).getCartas()).withRel("todas")
        );

        return ResponseEntity.status(HttpStatus.OK).body(collection);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

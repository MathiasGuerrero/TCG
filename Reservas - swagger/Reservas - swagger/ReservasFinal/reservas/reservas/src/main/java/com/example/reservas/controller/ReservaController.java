package com.example.reservas.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.example.reservas.model.Reserva;
import com.example.reservas.repository.ReservaRepository;
import com.example.reservas.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RequestMapping("/api/v1/reservas")
@RestController
public class ReservaController {

    @Autowired
    private ReservaService service;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Reserva>>> listar() {
        List<EntityModel<Reserva>> reservas = service.getReservas()
                .stream()
                .map(reserva -> EntityModel.of(reserva,
                        linkTo(methodOn(ReservaController.class).findById(reserva.getId())).withSelfRel(),
                        linkTo(methodOn(ReservaController.class).listar()).withRel("todas"),
                        linkTo(methodOn(ReservaController.class).buscarPorTipoPago(reserva.getTipoPago())).withRel("porTipoPago")
                ))
                .toList();

        CollectionModel<EntityModel<Reserva>> collection = CollectionModel.of(reservas,
                linkTo(methodOn(ReservaController.class).listar()).withSelfRel()
        );

        return ResponseEntity.status(HttpStatus.OK).body(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Reserva>> findById(@PathVariable Long id) {
        Reserva reserva = service.findById(id);

        EntityModel<Reserva> resource = EntityModel.of(reserva,
                linkTo(methodOn(ReservaController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(ReservaController.class).listar()).withRel("todas"),
                linkTo(methodOn(ReservaController.class).buscarPorTipoPago(reserva.getTipoPago())).withRel("porTipoPago")
        );

        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }

    @GetMapping("/tipopago/{tipoPago}")
    public ResponseEntity<CollectionModel<EntityModel<Reserva>>> buscarPorTipoPago(@RequestParam String tipoPago) {
        List<EntityModel<Reserva>> reservas = service.findByTipoPago(tipoPago)
                .stream()
                .map(reserva -> EntityModel.of(reserva,
                        linkTo(methodOn(ReservaController.class).findById(reserva.getId())).withSelfRel(),
                        linkTo(methodOn(ReservaController.class).listar()).withRel("todas")
                ))
                .toList();

        CollectionModel<EntityModel<Reserva>> collection = CollectionModel.of(reservas,
                linkTo(methodOn(ReservaController.class).buscarPorTipoPago(tipoPago)).withSelfRel(),
                linkTo(methodOn(ReservaController.class).listar()).withRel("todas")
        );

        return ResponseEntity.status(HttpStatus.OK).body(collection);
    }

    @PostMapping
    public ResponseEntity<EntityModel<Reserva>> crearReserva(@RequestBody Reserva reserva) {
        Reserva reservaCreada = service.crear(reserva);

        EntityModel<Reserva> resource = EntityModel.of(reservaCreada,
                linkTo(methodOn(ReservaController.class).findById(reservaCreada.getId())).withSelfRel(),
                linkTo(methodOn(ReservaController.class).listar()).withRel("todas"),
                linkTo(methodOn(ReservaController.class).buscarPorTipoPago(reservaCreada.getTipoPago())).withRel("porTipoPago")
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }
}

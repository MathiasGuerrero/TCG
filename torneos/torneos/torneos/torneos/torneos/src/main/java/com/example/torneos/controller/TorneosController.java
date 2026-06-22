package com.example.torneos.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.example.torneos.model.Torneos;
import com.example.torneos.service.TorneoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/v1/torneos")
@RestController
public class TorneosController {

    @Autowired
    private TorneoService service;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Torneos>>> listar() {
        List<EntityModel<Torneos>> torneos = service.getTorneos()
                .stream()
                .map(torneo -> EntityModel.of(torneo,
                        linkTo(methodOn(TorneosController.class).findById(torneo.getId())).withSelfRel(),
                        linkTo(methodOn(TorneosController.class).listar()).withRel("todos"),
                        linkTo(methodOn(TorneosController.class).buscarPorNombre(torneo.getNombreTorneo())).withRel("porNombre"),
                        linkTo(methodOn(TorneosController.class).buscarPorParticipantes(torneo.getParticipantes())).withRel("porParticipantes"),
                        linkTo(methodOn(TorneosController.class).buscarPorDuracion(torneo.getDuracion())).withRel("porDuracion")
                ))
                .toList();

        CollectionModel<EntityModel<Torneos>> collection = CollectionModel.of(torneos,
                linkTo(methodOn(TorneosController.class).listar()).withSelfRel()
        );

        return ResponseEntity.status(HttpStatus.OK).body(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Torneos>> findById(@PathVariable Long id) {
        Torneos torneo = service.findById(id);

        EntityModel<Torneos> resource = EntityModel.of(torneo,
                linkTo(methodOn(TorneosController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(TorneosController.class).listar()).withRel("todos"),
                linkTo(methodOn(TorneosController.class).buscarPorNombre(torneo.getNombreTorneo())).withRel("porNombre"),
                linkTo(methodOn(TorneosController.class).buscarPorParticipantes(torneo.getParticipantes())).withRel("porParticipantes"),
                linkTo(methodOn(TorneosController.class).buscarPorDuracion(torneo.getDuracion())).withRel("porDuracion")
        );

        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }

    @GetMapping("/participantes")
    public ResponseEntity<CollectionModel<EntityModel<Torneos>>> buscarPorParticipantes(@RequestParam int participantes) {
        List<EntityModel<Torneos>> torneos = service.findByParticipantes(participantes)
                .stream()
                .map(torneo -> EntityModel.of(torneo,
                        linkTo(methodOn(TorneosController.class).findById(torneo.getId())).withSelfRel(),
                        linkTo(methodOn(TorneosController.class).listar()).withRel("todos"),
                        linkTo(methodOn(TorneosController.class).buscarPorNombre(torneo.getNombreTorneo())).withRel("porNombre"),
                        linkTo(methodOn(TorneosController.class).buscarPorDuracion(torneo.getDuracion())).withRel("porDuracion")
                ))
                .toList();

        CollectionModel<EntityModel<Torneos>> collection = CollectionModel.of(torneos,
                linkTo(methodOn(TorneosController.class).buscarPorParticipantes(participantes)).withSelfRel(),
                linkTo(methodOn(TorneosController.class).listar()).withRel("todos")
        );

        return ResponseEntity.status(HttpStatus.OK).body(collection);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<EntityModel<Torneos>> buscarPorNombre(@PathVariable String nombre) {
        Torneos torneo = service.findByNombre(nombre);

        EntityModel<Torneos> resource = EntityModel.of(torneo,
                linkTo(methodOn(TorneosController.class).buscarPorNombre(nombre)).withSelfRel(),
                linkTo(methodOn(TorneosController.class).findById(torneo.getId())).withRel("porId"),
                linkTo(methodOn(TorneosController.class).listar()).withRel("todos"),
                linkTo(methodOn(TorneosController.class).buscarPorParticipantes(torneo.getParticipantes())).withRel("porParticipantes"),
                linkTo(methodOn(TorneosController.class).buscarPorDuracion(torneo.getDuracion())).withRel("porDuracion")
        );

        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }

    @GetMapping("/duracion/{duracion}")
    public ResponseEntity<CollectionModel<EntityModel<Torneos>>> buscarPorDuracion(@RequestParam int duracion) {
        List<EntityModel<Torneos>> torneos = service.findByDuracion(duracion)
                .stream()
                .map(torneo -> EntityModel.of(torneo,
                        linkTo(methodOn(TorneosController.class).findById(torneo.getId())).withSelfRel(),
                        linkTo(methodOn(TorneosController.class).listar()).withRel("todos"),
                        linkTo(methodOn(TorneosController.class).buscarPorNombre(torneo.getNombreTorneo())).withRel("porNombre"),
                        linkTo(methodOn(TorneosController.class).buscarPorParticipantes(torneo.getParticipantes())).withRel("porParticipantes")
                ))
                .toList();

        CollectionModel<EntityModel<Torneos>> collection = CollectionModel.of(torneos,
                linkTo(methodOn(TorneosController.class).buscarPorDuracion(duracion)).withSelfRel(),
                linkTo(methodOn(TorneosController.class).listar()).withRel("todos")
        );

        return ResponseEntity.status(HttpStatus.OK).body(collection);
    }

    @PostMapping
    public ResponseEntity<EntityModel<Torneos>> crearTorneo(@RequestBody Torneos torneos) {
        Torneos torneoCreado = service.crear(torneos);

        EntityModel<Torneos> resource = EntityModel.of(torneoCreado,
                linkTo(methodOn(TorneosController.class).findById(torneoCreado.getId())).withSelfRel(),
                linkTo(methodOn(TorneosController.class).listar()).withRel("todos"),
                linkTo(methodOn(TorneosController.class).buscarPorNombre(torneoCreado.getNombreTorneo())).withRel("porNombre"),
                linkTo(methodOn(TorneosController.class).buscarPorParticipantes(torneoCreado.getParticipantes())).withRel("porParticipantes"),
                linkTo(methodOn(TorneosController.class).buscarPorDuracion(torneoCreado.getDuracion())).withRel("porDuracion")
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }
}
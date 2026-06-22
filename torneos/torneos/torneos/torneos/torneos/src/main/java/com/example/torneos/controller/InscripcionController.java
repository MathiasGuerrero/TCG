package com.example.torneos.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.example.torneos.model.Inscripcion;
import com.example.torneos.service.InscripcionService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/torneos")
@RequiredArgsConstructor
public class InscripcionController {

    private final InscripcionService service;

    @PostMapping("/{torneoId}/inscribir")
    public ResponseEntity<EntityModel<Inscripcion>> inscribir(
            @PathVariable Long torneoId,
            @RequestParam Long usuarioId) {

        Inscripcion inscripcion = service.inscribir(torneoId, usuarioId);

        EntityModel<Inscripcion> resource = EntityModel.of(inscripcion,
                linkTo(methodOn(InscripcionController.class).getInscritos(torneoId)).withRel("inscritos"),
                linkTo(methodOn(InscripcionController.class).desinscribir(torneoId, usuarioId)).withRel("desinscribir")
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @GetMapping("/{torneoId}/inscritos")
    public ResponseEntity<CollectionModel<EntityModel<Inscripcion>>> getInscritos(@PathVariable Long torneoId) {
        List<EntityModel<Inscripcion>> inscritos = service.getInscritos(torneoId)
                .stream()
                .map(inscripcion -> EntityModel.of(inscripcion,
                        linkTo(methodOn(InscripcionController.class).getInscritos(torneoId)).withSelfRel(),
                        linkTo(methodOn(InscripcionController.class).desinscribir(torneoId, inscripcion.getUsuarioId())).withRel("desinscribir"),
                        linkTo(methodOn(InscripcionController.class).inscribir(torneoId, inscripcion.getUsuarioId())).withRel("reinscribir")
                ))
                .toList();

        CollectionModel<EntityModel<Inscripcion>> collection = CollectionModel.of(inscritos,
                linkTo(methodOn(InscripcionController.class).getInscritos(torneoId)).withSelfRel(),
                linkTo(methodOn(InscripcionController.class).inscribir(torneoId, null)).withRel("inscribir")
        );

        return ResponseEntity.status(HttpStatus.OK).body(collection);
    }

    @DeleteMapping("/{torneoId}/desinscribir")
    public ResponseEntity<Void> desinscribir(
            @PathVariable Long torneoId,
            @RequestParam Long usuarioId) {
        service.desinscribir(torneoId, usuarioId);
        return ResponseEntity.noContent().build();
    }
}
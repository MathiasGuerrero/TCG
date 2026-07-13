package com.example.torneos.controller;

import com.example.torneos.model.Inscripcion;
import com.example.torneos.service.InscripcionService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Inscripcion> inscribir(
            @PathVariable Long torneoId,
            @RequestParam Long usuarioId) {

        Inscripcion inscripcion = service.inscribir(torneoId, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(inscripcion);
    }

    @GetMapping("/{torneoId}/inscritos")
    public ResponseEntity<List<Inscripcion>> getInscritos(@PathVariable Long torneoId) {
        List<Inscripcion> inscritos = service.getInscritos(torneoId);
        return ResponseEntity.status(HttpStatus.OK).body(inscritos);
    }

    @DeleteMapping("/{torneoId}/desinscribir")
    public ResponseEntity<Void> desinscribir(
            @PathVariable Long torneoId,
            @RequestParam Long usuarioId) {
        service.desinscribir(torneoId, usuarioId);
        return ResponseEntity.noContent().build();
    }
}
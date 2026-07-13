package com.example.torneos.controller;

import com.example.torneos.model.Torneos;
import com.example.torneos.service.TorneoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/torneos")
@RestController
public class TorneosController {

    @Autowired
    private TorneoService service;

    @GetMapping
    public ResponseEntity<List<Torneos>> listar() {
        List<Torneos> torneos = service.getTorneos();
        return ResponseEntity.status(HttpStatus.OK).body(torneos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Torneos> findById(@PathVariable Long id) {
        Torneos torneo = service.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(torneo);
    }

    @GetMapping("/participantes")
    public ResponseEntity<List<Torneos>> buscarPorParticipantes(@RequestParam int participantes) {
        List<Torneos> torneos = service.findByParticipantes(participantes);
        return ResponseEntity.status(HttpStatus.OK).body(torneos);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Torneos> buscarPorNombre(@PathVariable String nombre) {
        Torneos torneo = service.findByNombre(nombre);
        return ResponseEntity.status(HttpStatus.OK).body(torneo);
    }

    @GetMapping("/duracion/{duracion}")
    public ResponseEntity<List<Torneos>> buscarPorDuracion(@PathVariable int duracion) {
        List<Torneos> torneos = service.findByDuracion(duracion);
        return ResponseEntity.status(HttpStatus.OK).body(torneos);
    }

    @PostMapping
    public ResponseEntity<Torneos> crearTorneo(@RequestBody Torneos torneos) {
        Torneos torneoCreado = service.crear(torneos);
        return ResponseEntity.status(HttpStatus.CREATED).body(torneoCreado);
    }
}
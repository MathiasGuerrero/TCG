package com.example.reservas.controller;

import com.example.reservas.model.Reserva;
import com.example.reservas.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/reservas")
@RestController
public class ReservaController {

    @Autowired
    private ReservaService service;

    @GetMapping
    public ResponseEntity<List<Reserva>> listar() {
        List<Reserva> reservas = service.getReservas();
        return ResponseEntity.status(HttpStatus.OK).body(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> findById(@PathVariable Long id) {
        Reserva reserva = service.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(reserva);
    }

    // Nota: Cambié @RequestParam a @PathVariable para que coincida exactamente
    // con la ruta de tu @GetMapping("/tipopago/{tipoPago}")
    @GetMapping("/tipopago/{tipoPago}")
    public ResponseEntity<List<Reserva>> buscarPorTipoPago(@PathVariable String tipoPago) {
        List<Reserva> reservas = service.findByTipoPago(tipoPago);
        return ResponseEntity.status(HttpStatus.OK).body(reservas);
    }

    @PostMapping
    public ResponseEntity<Reserva> crearReserva(@RequestBody Reserva reserva) {
        Reserva reservaCreada = service.crear(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaCreada);
    }
}
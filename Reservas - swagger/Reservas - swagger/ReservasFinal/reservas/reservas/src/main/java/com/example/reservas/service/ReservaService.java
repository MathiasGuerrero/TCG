package com.example.reservas.service;


import com.example.reservas.model.Reserva;
import com.example.reservas.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository repository;

    public Reserva crear(Reserva reserva){

        return repository.save(reserva);
    }

    public Reserva findById(Long id){

        Reserva reserva = repository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Reserva no encontrada"
        ));

        return reserva;
    }

    public List<Reserva> getReservas(){
        List<Reserva> reservas = repository.findAll();

        if (reservas.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No hay reservas regitradas"
            );
        }

        return reservas;
    }

    public List<Reserva> findByTipoPago(String tipoPago){

        List<Reserva> reservas = repository.findByTipoPago(tipoPago);

        if (reservas.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No hay reservas registradas con el tipo de pago ingresado"
            );
        }

        return repository.findByTipoPago(tipoPago);
    }
}

package com.example.torneos.service;


import com.example.torneos.model.Torneos;
import com.example.torneos.repository.TorneoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TorneoService {

    @Autowired
    private TorneoRepository repository;

    public Torneos crear(Torneos torneos){

        return (repository.save(torneos));
    }

    public Torneos findById(Long id){
        Torneos torneo = repository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "No existe torneo con el ID ingresado"
        ));

        return torneo;
    }

    public Torneos findByNombre(String nombre){

        Torneos torneos = repository.findByNombreTorneo(nombre).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "No existe torneo con el nombre ingresado"
        ));


        return torneos;
    }

    public List<Torneos> getTorneos(){
        List<Torneos> torneos =  repository.findAll();

        if (torneos.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No hay torneos registrados"
            );
        }

        return torneos;
    }

    public List<Torneos> findByParticipantes(int participantes){

        List<Torneos> torneos = repository.findByParticipantes(participantes);

        if (torneos.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No hay torneos registrados con la cantidad de participantes ingresada"
            );
        }

        return repository.findByParticipantes(participantes);
    }

    public List<Torneos> findByDuracion(int duracion){
        return repository.findByDuracion(duracion);
    }


}

package com.example.FireTCG_Store.service;


import com.example.FireTCG_Store.model.Carta;
import com.example.FireTCG_Store.repository.CartaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CartaService {

    @Autowired
    private CartaRepository repository;


    public Carta crear(Carta carta){
        return repository.save(carta);
    }

    public List<Carta> getCartas(){

        return repository.findAll();
    }

    public Carta filtrarById(Long id){
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Carta no encontrada"
        ));
    }

    public List<Carta> filtrarByNombre(String nombre){
        List<Carta> cartas = repository.findByNombre(nombre);

        if (cartas.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No se encontraron cartas con el nombre ingresado"
            );
        }
        return cartas;
    }

    public List<Carta> filtrarByTcg(String tcg){

        List<Carta> cartas = repository.findByTcg(tcg);

        if (cartas.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No se encontraron cartas del TCG ingresado"
            );
        }

        return cartas;
    }

    public List<Carta> filtrarByAnio(Integer anio){

        List<Carta> cartas = repository.findByAnio(anio);

        if (cartas.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No se encontraron cartas del año ingresado"
            );
        }

        return cartas;
    }

    public void deleteById(Long id){

        repository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "No existe carta con el Id ingresado"
        ));

        repository.deleteById(id);
    }

}

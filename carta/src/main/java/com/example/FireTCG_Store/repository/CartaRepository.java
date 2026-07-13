package com.example.FireTCG_Store.repository;

import com.example.FireTCG_Store.model.Carta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartaRepository extends JpaRepository<Carta,Long> {

    List<Carta> findByNombre(String nombre);

    List<Carta> findByTcg(String tcg);

    List<Carta> findByAnio(Integer anio);


}

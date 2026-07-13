package com.example.torneos.repository;


import com.example.torneos.model.Torneos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TorneoRepository extends JpaRepository<Torneos,Long> {

    Optional<Torneos> findById(Long id);

    Optional<Torneos> findByNombreTorneo(String nombre);

    List<Torneos> findByDuracion(int duracion);

    List<Torneos> findByParticipantes(int participantes);
}

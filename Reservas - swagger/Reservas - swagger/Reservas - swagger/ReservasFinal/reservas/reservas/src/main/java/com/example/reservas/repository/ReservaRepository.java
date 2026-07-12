package com.example.reservas.repository;

import com.example.reservas.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ReservaRepository extends JpaRepository<Reserva,Long> {


    Optional<Reserva> findById(Long id);

    List<Reserva> findByTipoPago(String tipoPago);




    
}

package com.example.torneos.repository;

import com.example.torneos.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    List<Inscripcion> findByTorneoId(Long torneoId);

    boolean existsByTorneoIdAndUsuarioId(Long torneoId, Long usuarioId);

    void deleteByTorneoIdAndUsuarioId(Long torneoId, Long usuarioId);
}

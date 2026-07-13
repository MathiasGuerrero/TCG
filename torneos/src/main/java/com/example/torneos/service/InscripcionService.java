package com.example.torneos.service;

import com.example.torneos.client.UsuarioClient;
import com.example.torneos.model.Inscripcion;
import com.example.torneos.model.Torneos;
import com.example.torneos.model.Usuario;
import com.example.torneos.repository.InscripcionRepository;
import com.example.torneos.repository.TorneoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final TorneoRepository torneoRepository;
    private final UsuarioClient usuarioClient;

    public Inscripcion inscribir(Long torneoId, Long usuarioId) {

        Torneos torneo = torneoRepository.findById(torneoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Torneo no encontrado"
                ));

        Usuario usuario = usuarioClient.obtenerUsuario(usuarioId).block();
        if (usuario == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuario no encontrado"
            );
        }

        if (inscripcionRepository.existsByTorneoIdAndUsuarioId(torneoId, usuarioId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "El usuario ya está inscrito en este torneo"
            );
        }

        long inscritos = inscripcionRepository.findByTorneoId(torneoId).size();
        if (inscritos >= torneo.getParticipantes()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El torneo ya está lleno"
            );
        }

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setTorneoId(torneoId);
        inscripcion.setUsuarioId(usuarioId);
        inscripcion.setUsernameUsuario(usuario.getUsername());

        return inscripcionRepository.save(inscripcion);
    }

    public List<Inscripcion> getInscritos(Long torneoId) {
        List<Inscripcion> inscritos = inscripcionRepository.findByTorneoId(torneoId);
        if (inscritos.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No hay usuarios inscritos en este torneo"
            );
        }
        return inscritos;
    }

    public void desinscribir(Long torneoId, Long usuarioId) {
        if (!inscripcionRepository.existsByTorneoIdAndUsuarioId(torneoId, usuarioId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "El usuario no está inscrito en este torneo"
            );
        }
        inscripcionRepository.deleteByTorneoIdAndUsuarioId(torneoId, usuarioId);
    }
}
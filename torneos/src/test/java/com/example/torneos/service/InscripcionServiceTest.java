package com.example.torneos.service;

import com.example.torneos.client.UsuarioClient;
import com.example.torneos.model.Inscripcion;
import com.example.torneos.model.Torneos;
import com.example.torneos.model.Usuario;
import com.example.torneos.repository.InscripcionRepository;
import com.example.torneos.repository.TorneoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InscripcionServiceTest {

    @Mock
    private InscripcionRepository inscripcionRepository;

    @Mock
    private TorneoRepository torneoRepository;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private InscripcionService inscripcionService;

    private Torneos torneo;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        torneo = new Torneos();
        torneo.setId(1L);
        torneo.setParticipantes(8);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("jdoe");
    }

    // ---------- inscribir() ----------

    @Test
    @DisplayName("inscribir: inscribe correctamente cuando torneo y usuario existen, no está inscrito y hay cupo")
    void deberiaInscribirCorrectamente() {
        when(torneoRepository.findById(1L)).thenReturn(Optional.of(torneo));
        when(usuarioClient.obtenerUsuario(1L)).thenReturn(Mono.just(usuario));
        when(inscripcionRepository.existsByTorneoIdAndUsuarioId(1L, 1L)).thenReturn(false);
        when(inscripcionRepository.findByTorneoId(1L)).thenReturn(List.of());
        when(inscripcionRepository.save(any(Inscripcion.class))).thenAnswer(inv -> inv.getArgument(0));

        Inscripcion resultado = inscripcionService.inscribir(1L, 1L);

        assertThat(resultado.getTorneoId()).isEqualTo(1L);
        assertThat(resultado.getUsuarioId()).isEqualTo(1L);
        assertThat(resultado.getUsernameUsuario()).isEqualTo("jdoe");
        verify(inscripcionRepository).save(any(Inscripcion.class));
    }

    @Test
    @DisplayName("inscribir: lanza 404 cuando el torneo no existe")
    void deberiaLanzar404SiTorneoNoExiste() {
        when(torneoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> inscripcionService.inscribir(99L, 1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Torneo no encontrado");

        verify(usuarioClient, never()).obtenerUsuario(anyLong());
        verify(inscripcionRepository, never()).save(any());
    }

    @Test
    @DisplayName("inscribir: lanza 404 cuando el usuario no existe")
    void deberiaLanzar404SiUsuarioNoExiste() {
        when(torneoRepository.findById(1L)).thenReturn(Optional.of(torneo));
        when(usuarioClient.obtenerUsuario(1L)).thenReturn(Mono.empty());

        assertThatThrownBy(() -> inscripcionService.inscribir(1L, 1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Usuario no encontrado");

        verify(inscripcionRepository, never()).existsByTorneoIdAndUsuarioId(anyLong(), anyLong());
        verify(inscripcionRepository, never()).save(any());
    }

    @Test
    @DisplayName("inscribir: lanza 409 cuando el usuario ya está inscrito")
    void deberiaLanzar409SiYaEstaInscrito() {
        when(torneoRepository.findById(1L)).thenReturn(Optional.of(torneo));
        when(usuarioClient.obtenerUsuario(1L)).thenReturn(Mono.just(usuario));
        when(inscripcionRepository.existsByTorneoIdAndUsuarioId(1L, 1L)).thenReturn(true);

        assertThatThrownBy(() -> inscripcionService.inscribir(1L, 1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("ya está inscrito");

        verify(inscripcionRepository, never()).findByTorneoId(anyLong());
        verify(inscripcionRepository, never()).save(any());
    }

    @Test
    @DisplayName("inscribir: lanza 400 cuando el torneo ya está lleno")
    void deberiaLanzar400SiTorneoEstaLleno() {
        torneo.setParticipantes(2);

        when(torneoRepository.findById(1L)).thenReturn(Optional.of(torneo));
        when(usuarioClient.obtenerUsuario(1L)).thenReturn(Mono.just(usuario));
        when(inscripcionRepository.existsByTorneoIdAndUsuarioId(1L, 1L)).thenReturn(false);
        when(inscripcionRepository.findByTorneoId(1L)).thenReturn(
                List.of(new Inscripcion(), new Inscripcion())
        );

        assertThatThrownBy(() -> inscripcionService.inscribir(1L, 1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("El torneo ya está lleno");

        verify(inscripcionRepository, never()).save(any());
    }

    // ---------- getInscritos() ----------

    @Test
    @DisplayName("getInscritos: retorna la lista cuando hay inscritos")
    void deberiaRetornarListaDeInscritos() {
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setTorneoId(1L);
        inscripcion.setUsuarioId(1L);
        inscripcion.setUsernameUsuario("jdoe");

        when(inscripcionRepository.findByTorneoId(1L)).thenReturn(List.of(inscripcion));

        List<Inscripcion> resultado = inscripcionService.getInscritos(1L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getUsernameUsuario()).isEqualTo("jdoe");
    }

    @Test
    @DisplayName("getInscritos: lanza 404 cuando no hay inscritos")
    void deberiaLanzar404SiNoHayInscritos() {
        when(inscripcionRepository.findByTorneoId(1L)).thenReturn(List.of());

        assertThatThrownBy(() -> inscripcionService.getInscritos(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No hay usuarios inscritos en este torneo");
    }

    // ---------- desinscribir() ----------

    @Test
    @DisplayName("desinscribir: elimina la inscripción cuando existe")
    void deberiaDesinscribirCorrectamente() {
        when(inscripcionRepository.existsByTorneoIdAndUsuarioId(1L, 1L)).thenReturn(true);

        inscripcionService.desinscribir(1L, 1L);

        verify(inscripcionRepository).deleteByTorneoIdAndUsuarioId(1L, 1L);
    }

    @Test
    @DisplayName("desinscribir: lanza 404 cuando la inscripción no existe")
    void deberiaLanzar404SiInscripcionNoExiste() {
        when(inscripcionRepository.existsByTorneoIdAndUsuarioId(1L, 1L)).thenReturn(false);

        assertThatThrownBy(() -> inscripcionService.desinscribir(1L, 1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("El usuario no está inscrito en este torneo");

        verify(inscripcionRepository, never()).deleteByTorneoIdAndUsuarioId(anyLong(), anyLong());
    }
}
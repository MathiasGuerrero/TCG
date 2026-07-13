package com.example.torneos.service;

import com.example.torneos.model.Torneos;
import com.example.torneos.repository.TorneoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TorneoServiceTest {

    @Mock
    private TorneoRepository repository;

    @InjectMocks
    private TorneoService torneoService;

    private Torneos torneo;

    @BeforeEach
    void setUp() {
        torneo = new Torneos();
        torneo.setId(1L);
        torneo.setNombreTorneo("Regional Santiago");
        torneo.setParticipantes(16);
        torneo.setDuracion(2);
    }

    // ---------- crear() ----------

    @Test
    @DisplayName("crear: guarda y retorna el torneo")
    void deberiaCrearTorneoCorrectamente() {
        when(repository.save(torneo)).thenReturn(torneo);

        Torneos resultado = torneoService.crear(torneo);

        assertThat(resultado).isEqualTo(torneo);
        assertThat(resultado.getNombreTorneo()).isEqualTo("Regional Santiago");
        verify(repository).save(torneo);
    }

    // ---------- findById() ----------

    @Test
    @DisplayName("findById: retorna el torneo cuando existe")
    void deberiaRetornarTorneoPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(torneo));

        Torneos resultado = torneoService.findById(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("findById: lanza 404 cuando no existe torneo con ese ID")
    void deberiaLanzar404SiTorneoNoExistePorId() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> torneoService.findById(99L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No existe torneo con el ID ingresado");
    }

    // ---------- findByNombre() ----------

    @Test
    @DisplayName("findByNombre: retorna el torneo cuando existe")
    void deberiaRetornarTorneoPorNombre() {
        when(repository.findByNombreTorneo("Regional Santiago")).thenReturn(Optional.of(torneo));

        Torneos resultado = torneoService.findByNombre("Regional Santiago");

        assertThat(resultado.getNombreTorneo()).isEqualTo("Regional Santiago");
    }

    @Test
    @DisplayName("findByNombre: lanza 404 cuando no existe torneo con ese nombre")
    void deberiaLanzar404SiTorneoNoExistePorNombre() {
        when(repository.findByNombreTorneo("Inexistente")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> torneoService.findByNombre("Inexistente"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No existe torneo con el nombre ingresado");
    }

    // ---------- getTorneos() ----------

    @Test
    @DisplayName("getTorneos: retorna la lista cuando hay torneos registrados")
    void deberiaRetornarListaDeTorneos() {
        when(repository.findAll()).thenReturn(List.of(torneo));

        List<Torneos> resultado = torneoService.getTorneos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombreTorneo()).isEqualTo("Regional Santiago");
    }

    @Test
    @DisplayName("getTorneos: lanza 404 cuando no hay torneos registrados")
    void deberiaLanzar404SiNoHayTorneos() {
        when(repository.findAll()).thenReturn(List.of());

        assertThatThrownBy(() -> torneoService.getTorneos())
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No hay torneos registrados");
    }

    // ---------- findByParticipantes() ----------
    // Corregido: ahora solo llama al repository una vez, reutilizando la variable.

    @Test
    @DisplayName("findByParticipantes: retorna la lista cuando hay torneos con esa cantidad de participantes")
    void deberiaRetornarTorneosPorParticipantes() {
        when(repository.findByParticipantes(16)).thenReturn(List.of(torneo));

        List<Torneos> resultado = torneoService.findByParticipantes(16);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getParticipantes()).isEqualTo(16);
        verify(repository, times(1)).findByParticipantes(16);
    }

    @Test
    @DisplayName("findByParticipantes: lanza 404 cuando no hay torneos con esa cantidad de participantes")
    void deberiaLanzar404SiNoHayTorneosConEsaCantidadDeParticipantes() {
        when(repository.findByParticipantes(100)).thenReturn(List.of());

        assertThatThrownBy(() -> torneoService.findByParticipantes(100))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No hay torneos registrados con la cantidad de participantes ingresada");
    }

    // ---------- findByDuracion() ----------
    // Corregido: ahora valida lista vacía y lanza 404, igual que los demás métodos de lista.

    @Test
    @DisplayName("findByDuracion: retorna la lista cuando hay torneos con esa duración")
    void deberiaRetornarTorneosPorDuracion() {
        when(repository.findByDuracion(2)).thenReturn(List.of(torneo));

        List<Torneos> resultado = torneoService.findByDuracion(2);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getDuracion()).isEqualTo(2);
    }

    @Test
    @DisplayName("findByDuracion: lanza 404 cuando no hay torneos con esa duración")
    void deberiaLanzar404SiNoHayTorneosConEsaDuracion() {
        when(repository.findByDuracion(99)).thenReturn(List.of());

        assertThatThrownBy(() -> torneoService.findByDuracion(99))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No hay torneos registrados con la duración ingresada");
    }
}
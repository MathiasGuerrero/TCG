package com.example.FireTCG_Store.service;

import com.example.FireTCG_Store.model.Carta;
import com.example.FireTCG_Store.repository.CartaRepository;
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
class CartaServiceTest {

    @Mock
    private CartaRepository repository;

    @InjectMocks
    private CartaService cartaService;

    private Carta carta;

    @BeforeEach
    void setUp() {
        carta = new Carta();
        carta.setId(1L);
        carta.setNombre("Charizard");
        carta.setTcg("Pokemon");
        carta.setAnio(1999);
    }

    // ---------- crear() ----------

    @Test
    @DisplayName("crear: guarda y retorna la carta")
    void deberiaCrearCartaCorrectamente() {
        when(repository.save(carta)).thenReturn(carta);

        Carta resultado = cartaService.crear(carta);

        assertThat(resultado.getNombre()).isEqualTo("Charizard");
        verify(repository).save(carta);
    }

    // ---------- getCartas() ----------

    @Test
    @DisplayName("getCartas: retorna la lista de cartas registradas")
    void deberiaRetornarListaDeCartas() {
        when(repository.findAll()).thenReturn(List.of(carta));

        List<Carta> resultado = cartaService.getCartas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Charizard");
    }

    @Test
    @DisplayName("getCartas: retorna lista vacía cuando no hay cartas (sin lanzar excepción)")
    void deberiaRetornarListaVaciaSiNoHayCartas() {
        when(repository.findAll()).thenReturn(List.of());

        List<Carta> resultado = cartaService.getCartas();

        assertThat(resultado).isEmpty();
    }

    // ---------- filtrarById() ----------

    @Test
    @DisplayName("filtrarById: retorna la carta cuando existe")
    void deberiaRetornarCartaPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(carta));

        Carta resultado = cartaService.filtrarById(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("filtrarById: lanza 404 cuando la carta no existe")
    void deberiaLanzar404SiCartaNoExistePorId() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartaService.filtrarById(99L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Carta no encontrada");
    }

    // ---------- filtrarByNombre() ----------

    @Test
    @DisplayName("filtrarByNombre: retorna la lista cuando existen cartas con ese nombre")
    void deberiaRetornarCartasPorNombre() {
        when(repository.findByNombre("Charizard")).thenReturn(List.of(carta));

        List<Carta> resultado = cartaService.filtrarByNombre("Charizard");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Charizard");
    }

    @Test
    @DisplayName("filtrarByNombre: lanza 404 cuando no hay cartas con ese nombre")
    void deberiaLanzar404SiNoHayCartasConEseNombre() {
        when(repository.findByNombre("Inexistente")).thenReturn(List.of());

        assertThatThrownBy(() -> cartaService.filtrarByNombre("Inexistente"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No se encontraron cartas con el nombre ingresado");
    }

    // ---------- filtrarByTcg() ----------

    @Test
    @DisplayName("filtrarByTcg: retorna la lista cuando existen cartas de ese TCG")
    void deberiaRetornarCartasPorTcg() {
        when(repository.findByTcg("Pokemon")).thenReturn(List.of(carta));

        List<Carta> resultado = cartaService.filtrarByTcg("Pokemon");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTcg()).isEqualTo("Pokemon");
    }

    @Test
    @DisplayName("filtrarByTcg: lanza 404 cuando no hay cartas de ese TCG")
    void deberiaLanzar404SiNoHayCartasDeEseTcg() {
        when(repository.findByTcg("Inexistente")).thenReturn(List.of());

        assertThatThrownBy(() -> cartaService.filtrarByTcg("Inexistente"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No se encontraron cartas del TCG ingresado");
    }

    // ---------- filtrarByAnio() ----------

    @Test
    @DisplayName("filtrarByAnio: retorna la lista cuando existen cartas de ese año")
    void deberiaRetornarCartasPorAnio() {
        when(repository.findByAnio(1999)).thenReturn(List.of(carta));

        List<Carta> resultado = cartaService.filtrarByAnio(1999);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getAnio()).isEqualTo(1999);
    }

    @Test
    @DisplayName("filtrarByAnio: lanza 404 cuando no hay cartas de ese año")
    void deberiaLanzar404SiNoHayCartasDeEseAnio() {
        when(repository.findByAnio(1899)).thenReturn(List.of());

        assertThatThrownBy(() -> cartaService.filtrarByAnio(1899))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No se encontraron cartas del año ingresado");
    }

    // ---------- deleteById() ----------

    @Test
    @DisplayName("deleteById: elimina la carta cuando existe")
    void deberiaEliminarCartaExistente() {
        when(repository.findById(1L)).thenReturn(Optional.of(carta));

        cartaService.deleteById(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteById: lanza 404 cuando la carta no existe")
    void deberiaLanzar404SiCartaNoExisteAlEliminar() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartaService.deleteById(99L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No existe carta con el Id ingresado");

        verify(repository, never()).deleteById(anyLong());
    }
}
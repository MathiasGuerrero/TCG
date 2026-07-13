package com.example.reservas.service;

import com.example.reservas.model.Reserva;
import com.example.reservas.repository.ReservaRepository;
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
class ReservaServiceTest {

    @Mock
    private ReservaRepository repository;

    @InjectMocks
    private ReservaService reservaService;

    private Reserva reserva;

    @BeforeEach
    void setUp() {
        reserva = new Reserva();
        reserva.setId(1L);
        reserva.setTipoPago("TARJETA");
    }

    // ---------- crear() ----------

    @Test
    @DisplayName("crear: guarda y retorna la reserva")
    void deberiaCrearReservaCorrectamente() {
        when(repository.save(reserva)).thenReturn(reserva);

        Reserva resultado = reservaService.crear(reserva);

        assertThat(resultado).isEqualTo(reserva);
        assertThat(resultado.getTipoPago()).isEqualTo("TARJETA");
        verify(repository).save(reserva);
    }

    // ---------- findById() ----------

    @Test
    @DisplayName("findById: retorna la reserva cuando existe")
    void deberiaRetornarReservaPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(reserva));

        Reserva resultado = reservaService.findById(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("findById: lanza 404 cuando la reserva no existe")
    void deberiaLanzar404SiReservaNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservaService.findById(99L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Reserva no encontrada");
    }

    // ---------- getReservas() ----------

    @Test
    @DisplayName("getReservas: retorna la lista cuando hay reservas")
    void deberiaRetornarListaDeReservas() {
        when(repository.findAll()).thenReturn(List.of(reserva));

        List<Reserva> resultado = reservaService.getReservas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTipoPago()).isEqualTo("TARJETA");
    }

    @Test
    @DisplayName("getReservas: lanza 404 cuando no hay reservas registradas")
    void deberiaLanzar404SiNoHayReservas() {
        when(repository.findAll()).thenReturn(List.of());

        assertThatThrownBy(() -> reservaService.getReservas())
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No hay reservas regitradas");
    }

    // ---------- findByTipoPago() ----------
    // Corregido: el método ahora reutiliza la variable "reservas", una sola llamada al repository.

    @Test
    @DisplayName("findByTipoPago: retorna la lista cuando existen reservas con ese tipo de pago")
    void deberiaRetornarReservasPorTipoPago() {
        when(repository.findByTipoPago("TARJETA")).thenReturn(List.of(reserva));

        List<Reserva> resultado = reservaService.findByTipoPago("TARJETA");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTipoPago()).isEqualTo("TARJETA");
        verify(repository, times(1)).findByTipoPago("TARJETA");
    }

    @Test
    @DisplayName("findByTipoPago: lanza 404 cuando no hay reservas con ese tipo de pago")
    void deberiaLanzar404SiNoHayReservasConEseTipoPago() {
        when(repository.findByTipoPago("EFECTIVO")).thenReturn(List.of());

        assertThatThrownBy(() -> reservaService.findByTipoPago("EFECTIVO"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No hay reservas registradas con el tipo de pago ingresado");
    }
}

package com.example.pedidos.client;

import com.example.pedidos.model.Reserva;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReservaClient {

    @Qualifier("reservaWebClient")
    private final WebClient reservaWebClient;

    public Mono<Reserva> obtenerReserva(Long id) {
        return reservaWebClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(HttpStatus.Series.CLIENT_ERROR::equals, response ->
                        Mono.error(new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Reserva no encontrada"
                        ))
                )
                .onStatus(HttpStatus.Series.SERVER_ERROR::equals, response ->
                        Mono.error(new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR, "Error en microservicio reservas"
                        ))
                )
                .bodyToMono(Reserva.class);
    }

    public Flux<Reserva> listarReservas() {
        return reservaWebClient.get()
                .uri("")
                .retrieve()
                .bodyToFlux(Reserva.class);
    }


    public Mono<Reserva> crearReserva(Reserva reservaRequest) {
        return reservaWebClient.post()
                .uri("")
                .bodyValue(reservaRequest)
                .retrieve()
                .onStatus(HttpStatus.Series.SERVER_ERROR::equals, response ->
                        Mono.error(new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR, "Error al crear reserva en el microservicio"
                        ))
                )
                .bodyToMono(Reserva.class);
    }
}
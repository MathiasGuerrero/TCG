package com.example.pedidos.client;


import com.example.pedidos.model.Producto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductoClient {

    @Qualifier("productoWebClient")
    private final WebClient productoWebClient;

    public Mono<Producto> obtenerproducto(Long id){
        return productoWebClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR, "Error en microservicio productos"
                        ))
                )
                .bodyToMono(Producto.class);
    }

    public Flux<Producto> obtenerTodos(){
        return productoWebClient.get()
                .uri("/")
                .retrieve()
                .bodyToFlux(Producto.class);
    }

}

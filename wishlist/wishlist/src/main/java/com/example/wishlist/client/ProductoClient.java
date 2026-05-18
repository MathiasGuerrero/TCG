package com.example.wishlist.client;


import com.example.wishlist.model.Producto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
public class ProductoClient {

    private final WebClient productoWebClient;

    public ProductoClient(@Qualifier("productoWebClient") WebClient productoWebClient){
        this.productoWebClient = productoWebClient;
    }

    public Mono<Producto> obtenerProducto(Long id) {
        return productoWebClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Producto no encontrado"
                        ))
                )
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR, "Error en microservicio producto"
                        ))
                )
                .bodyToMono(Producto.class);
    }
}

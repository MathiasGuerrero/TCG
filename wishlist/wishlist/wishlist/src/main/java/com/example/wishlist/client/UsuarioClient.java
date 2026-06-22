package com.example.wishlist.client;


import com.example.wishlist.model.Usuario;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
public class UsuarioClient {

    private final WebClient usuarioWebClient;

    public UsuarioClient(@Qualifier("usuarioWebClient") WebClient usuarioWebClient) {
        this.usuarioWebClient = usuarioWebClient;
    }

    public Mono<Usuario> obtenerUsuario(Long id) {
        return usuarioWebClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Usuario no encontrado"
                        ))
                )
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR, "Error en microservicio usuario"
                        ))
                )
                .bodyToMono(Usuario.class);
    }
}

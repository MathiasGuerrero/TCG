package com.example.pedidos.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {

    @Bean
    public WebClient productoWebClient(){
        return WebClient.builder()
                .baseUrl("http://localhost:8082/api/v1/productos")
                .build();
    }

    @Bean
    public WebClient usuarioWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:9091/api/v1/usuario")
                .build();
    }

    @Bean
    public WebClient reservaWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8083/api/v1/reservas")
                .build();
    }
}

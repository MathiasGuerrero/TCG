package com.example.torneos.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient usuarioWebClient() {
        return WebClient.builder()
                .baseUrl("Http://localhost:9091/api/v1/usuario")
                .build();
    }
}

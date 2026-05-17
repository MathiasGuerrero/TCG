package com.example.pedidos.controller;


import com.example.pedidos.model.Pedido;
import com.example.pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/v1/pedidos")
@RestController
public class PedidoController {

        @Autowired
        private PedidoService service;

        @GetMapping("/{producto}")
        public Optional<List<Pedido>> findByProducto(@PathVariable String producto){
            return service.fitrarByProducto(producto);
        }

        @PostMapping
        public ResponseEntity<Pedido> crear(
                @RequestParam Long productoId,
                @RequestParam Integer cantidad) {

            Pedido pedido = service.crearPedido(productoId, cantidad);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
        }




}

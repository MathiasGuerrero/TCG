package com.example.pedidos.controller;


import com.example.pedidos.model.Pedido;
import com.example.pedidos.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/v1/pedidos")
@RestController
public class PedidoController {

        @Autowired
        private PedidoService service;

        @PostMapping
        public ResponseEntity<Pedido> crear(@RequestBody Pedido nuevoPedido){
            Pedido pedido = service.crear(nuevoPedido);
            return ResponseEntity.ok(pedido);
        }

        @GetMapping("/{producto}")
        public Optional<List<Pedido>> findByProducto(@PathVariable String producto){
        return service.fitrarByProducto(producto);
    }





}

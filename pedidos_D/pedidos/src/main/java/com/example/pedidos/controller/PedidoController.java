package com.example.pedidos.controller;


import com.example.pedidos.dto.PedidoRequest;
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
        public ResponseEntity<List<Pedido>> findByUsuario(@PathVariable Long usuarioID){
            List<Pedido> pedidos = service.fitrarByUsuarioID(usuarioID);
            return ResponseEntity.status(HttpStatus.OK).body(pedidos);
        }

        @PostMapping
        public ResponseEntity<Pedido> crear(@RequestBody PedidoRequest request) {
            Pedido pedido = service.crearPedido(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
        }




}

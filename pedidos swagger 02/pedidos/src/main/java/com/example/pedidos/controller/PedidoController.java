package com.example.pedidos.controller;

import com.example.pedidos.model.Pedido;
import com.example.pedidos.dto.PedidoRequest;
import com.example.pedidos.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService service;

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<Pedido>> findByUsuario(@PathVariable Long usuarioId) {
        List<Pedido> pedidos = service.filtrarByUsuarioID(usuarioId);
        return ResponseEntity.ok(pedidos);
    }

    @PostMapping
    public ResponseEntity<Pedido> crear(@RequestBody PedidoRequest request) {
        Pedido pedido = service.crearPedido(request);
        return ResponseEntity.ok(pedido);
    }
}

package com.example.pedidos.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.example.pedidos.dto.PedidoRequest;
import com.example.pedidos.model.Pedido;
import com.example.pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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

    @GetMapping("/{usuarioID}")
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> findByUsuario(
            @PathVariable Long usuarioID) {

        List<Pedido> pedidos = service.fitrarByUsuarioID(usuarioID);

        List<EntityModel<Pedido>> pedidosConLinks = pedidos.stream()
                .map(pedido -> EntityModel.of(pedido,
                        linkTo(methodOn(PedidoController.class)
                                .findByUsuario(usuarioID)).withSelfRel(),
                        linkTo(methodOn(PedidoController.class)
                                .crear(null)).withRel("crear-pedido")
                ))
                .toList();

        CollectionModel<EntityModel<Pedido>> resultado = CollectionModel.of(
                pedidosConLinks,
                linkTo(methodOn(PedidoController.class)
                        .findByUsuario(usuarioID)).withSelfRel()
        );

        return ResponseEntity.ok(resultado);
    }

    @PostMapping
    public ResponseEntity<EntityModel<Pedido>> crear(
            @RequestBody PedidoRequest request) {

        Pedido pedido = service.crearPedido(request);

        EntityModel<Pedido> pedidoConLinks = EntityModel.of(pedido,
                linkTo(methodOn(PedidoController.class)
                        .findByUsuario(pedido.getUsuarioId())).withRel("pedidos-usuario"),
                linkTo(methodOn(PedidoController.class)
                        .crear(null)).withSelfRel()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoConLinks);
    }
}

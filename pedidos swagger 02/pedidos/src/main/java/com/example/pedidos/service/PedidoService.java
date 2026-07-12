package com.example.pedidos.service;


import com.example.pedidos.client.ProductoClient;
import com.example.pedidos.client.ReservaClient;
import com.example.pedidos.client.UsuarioClient;
import com.example.pedidos.dto.DetallePedidoRequest;
import com.example.pedidos.dto.PedidoRequest;
import com.example.pedidos.model.DetallePedido;
import com.example.pedidos.model.Pedido;
import com.example.pedidos.model.Producto;
import com.example.pedidos.model.Usuario;
import com.example.pedidos.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PedidoService {


    private final PedidoRepository repository;

    private final ProductoClient productoClient;

    private final UsuarioClient usuarioClient;

    private final ReservaClient reservaClient;

    public Pedido crear(Pedido pedido){
        return repository.save(pedido);
    }


    public List<Pedido> getPedidos(){
        return repository.findAll();
    }

    public List<Pedido> filtrarByUsuarioID(Long usuarioId) {
        List<Pedido> pedidos = repository.findByUsuarioId(usuarioId);
        if (pedidos.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No se encontraron pedidos del usuario ingresado"
            );
        }

        return pedidos;
    }

    public Pedido crearPedido(PedidoRequest request){

        Usuario usuario = usuarioClient
                .obtenerUsuario(request.getUsuarioId())
                .block();

        if (usuario == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuario no encontrado"
            );
        }

        Pedido pedido = new Pedido();

        pedido.setUsuarioId(usuario.getId());
        pedido.setUsernameUsuario(usuario.getUsername());

        List<DetallePedido> detalles = new ArrayList<>();

        for (DetallePedidoRequest detalleReq : request.getDetalles()) {

            Producto producto = productoClient
                    .obtenerproducto(detalleReq.getProductoId())
                    .block();

            if (producto == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado"
                );
            }

            if (producto.getStock() < detalleReq.getCantidad()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Stock insuficiente para " + producto.getNombre()
                );
            }

            DetallePedido detalle = new DetallePedido();
            detalle.setProductoId(producto.getId());
            detalle.setNombreProducto(producto.getNombre());
            detalle.setCantidad(detalleReq.getCantidad());
            detalle.setPedido(pedido);

            detalles.add(detalle);
        }

        pedido.setDetalles(detalles);
        return repository.save(pedido);
    }









}

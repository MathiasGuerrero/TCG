package com.example.pedidos.service;


import com.example.pedidos.client.ProductoClient;
import com.example.pedidos.model.Pedido;
import com.example.pedidos.model.Producto;
import com.example.pedidos.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository repository;

    private final ProductoClient productoClient;

    public Pedido crear(Pedido pedido){
        return repository.save(pedido);
    }


    public List<Pedido> getPedidos(){
        return repository.findAll();
    }

    public Optional<List<Pedido>> fitrarByProducto(String producto){
            return repository.findByProducto(producto);
    }

    public Pedido crearPedido(Long productoId, Integer cantidad){
        Producto producto = productoClient
                .obtenerproducto(productoId)
                .block();

        if (producto.getStock() < cantidad){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Stock insuficiente");
        }

        Pedido pedido = new Pedido();
        pedido.setProducto(producto.getNombre());
        pedido.setCantidad(cantidad);

        return repository.save(pedido);
    }




}

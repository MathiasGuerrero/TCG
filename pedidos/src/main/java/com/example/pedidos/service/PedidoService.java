package com.example.pedidos.service;


import com.example.pedidos.model.Pedido;
import com.example.pedidos.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

        public Pedido crear(Pedido pedido){
        return repository.save(pedido);
    }


        public List<Pedido> getPedidos(){
        return repository.findAll();
    }

        public Optional<List<Pedido>> fitrarByProducto(String producto){
            return repository.findByProducto(producto);
        }


}

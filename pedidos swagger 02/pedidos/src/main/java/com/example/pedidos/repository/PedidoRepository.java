package com.example.pedidos.repository;

import com.example.pedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido,Long> {

    List<Pedido> findByUsuarioId(Long usuarioId);

    List<Pedido> findByUsernameUsuario(String username);
}

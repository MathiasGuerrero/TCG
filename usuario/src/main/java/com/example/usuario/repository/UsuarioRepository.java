package com.example.usuario.repository;

import com.example.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    Optional<List<Usuario>> findByUserIgnoreCase(String user);

    Optional<Usuario> findById(Long id);

    Optional<Usuario> findByCorreo(String correo);


}

package com.example.usuario.service;

import com.example.usuario.model.Usuario;
import com.example.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public Optional<List<Usuario>> findByUsername(String nombre) {
        return repository.findByUsernameIgnoreCase(nombre);
    }

    public Optional<Usuario> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<Usuario> findByCorreo(String correo) {
        return repository.findByCorreo(correo);
    }

    public Optional<Usuario> crear(Usuario usuario){
        return Optional.of(repository.save(usuario));
    }

    public List<Usuario> getUsuarios(){
        return repository.findAll();
    }


}

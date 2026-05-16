package com.example.usuario.service;

import com.example.usuario.model.Usuario;
import com.example.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public Optional<Usuario> findByUsername(String nombre) {
        return repository.findByUsernameIgnoreCase(nombre);
    }

    public Optional<Usuario> findById(Long id) {
        return Optional.of(
                repository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Usuario no encontrado"
                        ))
        );
    }

    public Optional<Usuario> findByCorreo(String correo) {
        return repository.findByCorreo(correo);
    }

    public Optional<Usuario> crear(Usuario usuario){

        if (repository.findByUsernameIgnoreCase(usuario.getUsername()).isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "El username ya está en uso"
            );
        } else if (repository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "El correo ya está en uso"
            );
        }else {

            return Optional.of(repository.save(usuario));
        }


    }

    public List<Usuario> getUsuarios(){
        return repository.findAll();
    }

    public void deleteById(Long id){

        if (repository.findById(id).isPresent()){
            repository.deleteById(id);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuario no encontrado"
            );
        }


    }


}

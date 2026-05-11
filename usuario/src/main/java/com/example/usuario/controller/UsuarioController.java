package com.example.usuario.controller;

import com.example.usuario.model.Usuario;
import com.example.usuario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public List<Usuario> listar(){
        return service.getUsuarios();
    }

/*
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Usuario>> findById(@PathVariable Long id){
        Usuario usuario = service.findById(id);
        return ResponseEntity.ok(usuario);
    }


    @GetMapping("/user/{user}")
    public ResponseEntity<List<Usuario>> findByUser(@PathVariable String user){
        List lista = service.findByUser(user);
        return ResponseEntity.ok(lista);
    }

*/
}

package com.example.usuario.controller;

import com.example.usuario.model.Usuario;
import com.example.usuario.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public List<Usuario> listar(){
        return service.getUsuarios();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Optional<Usuario>> findById(@PathVariable Long id){
        Optional<Usuario> usuario = service.findById(id);
        return ResponseEntity.ok(usuario);
    }


    @GetMapping("/user/{user}")
    public ResponseEntity<Optional<List<Usuario>>> findByUser(@PathVariable String user){
        Optional<List<Usuario>> lista = service.findByUsername(user);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<Optional<Usuario>> findByCorreo(@PathVariable String correo){
        Optional<Usuario> user = service.findByCorreo(correo);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<Optional<Usuario>> crearUsuario(@RequestBody Usuario user){
        Optional<Usuario> usuarioCreado = service.crear(user);
        return ResponseEntity.ok(usuarioCreado);
    }
/*
    @DeleteMapping("/{id}")
    public ResponseEntity borrarUsuario(@PathVariable Long id){

    }
*/


}
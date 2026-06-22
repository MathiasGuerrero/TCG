package com.example.usuario.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.example.usuario.model.Usuario;
import com.example.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> listar() {
        List<EntityModel<Usuario>> usuarios = service.getUsuarios()
                .stream()
                .map(usuario -> EntityModel.of(usuario,
                        linkTo(methodOn(UsuarioController.class).findById(usuario.getId())).withSelfRel(),
                        linkTo(methodOn(UsuarioController.class).listar()).withRel("todos"),
                        linkTo(methodOn(UsuarioController.class).findByUser(usuario.getUsername())).withRel("porUsername"),
                        linkTo(methodOn(UsuarioController.class).findByCorreo(usuario.getCorreo())).withRel("porCorreo"),
                        linkTo(methodOn(UsuarioController.class).borrarUsuario(usuario.getId())).withRel("eliminar")
                ))
                .toList();

        CollectionModel<EntityModel<Usuario>> collection = CollectionModel.of(usuarios,
                linkTo(methodOn(UsuarioController.class).listar()).withSelfRel()
        );

        return ResponseEntity.status(HttpStatus.OK).body(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> findById(@PathVariable Long id) {
        Usuario usuario = service.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"
                ));

        EntityModel<Usuario> resource = EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listar()).withRel("todos"),
                linkTo(methodOn(UsuarioController.class).findByUser(usuario.getUsername())).withRel("porUsername"),
                linkTo(methodOn(UsuarioController.class).findByCorreo(usuario.getCorreo())).withRel("porCorreo"),
                linkTo(methodOn(UsuarioController.class).borrarUsuario(id)).withRel("eliminar")
        );

        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }

    @GetMapping("/user/{user}")
    public ResponseEntity<EntityModel<Usuario>> findByUser(@PathVariable String user) {
        Usuario usuario = service.findByUsername(user)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"
                ));

        EntityModel<Usuario> resource = EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).findByUser(user)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).findById(usuario.getId())).withRel("porId"),
                linkTo(methodOn(UsuarioController.class).findByCorreo(usuario.getCorreo())).withRel("porCorreo"),
                linkTo(methodOn(UsuarioController.class).listar()).withRel("todos"),
                linkTo(methodOn(UsuarioController.class).borrarUsuario(usuario.getId())).withRel("eliminar")
        );

        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<EntityModel<Usuario>> findByCorreo(@PathVariable String correo) {
        Usuario usuario = service.findByCorreo(correo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"
                ));

        EntityModel<Usuario> resource = EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).findByCorreo(correo)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).findById(usuario.getId())).withRel("porId"),
                linkTo(methodOn(UsuarioController.class).findByUser(usuario.getUsername())).withRel("porUsername"),
                linkTo(methodOn(UsuarioController.class).listar()).withRel("todos"),
                linkTo(methodOn(UsuarioController.class).borrarUsuario(usuario.getId())).withRel("eliminar")
        );

        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }

    @PostMapping
    public ResponseEntity<EntityModel<Usuario>> crearUsuario(@Valid @RequestBody Usuario user) {
        Usuario usuarioCreado = service.crear(user)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.CONFLICT, "El usuario ya existe"
                ));

        EntityModel<Usuario> resource = EntityModel.of(usuarioCreado,
                linkTo(methodOn(UsuarioController.class).findById(usuarioCreado.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listar()).withRel("todos"),
                linkTo(methodOn(UsuarioController.class).findByUser(usuarioCreado.getUsername())).withRel("porUsername"),
                linkTo(methodOn(UsuarioController.class).findByCorreo(usuarioCreado.getCorreo())).withRel("porCorreo"),
                linkTo(methodOn(UsuarioController.class).borrarUsuario(usuarioCreado.getId())).withRel("eliminar")
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarUsuario(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
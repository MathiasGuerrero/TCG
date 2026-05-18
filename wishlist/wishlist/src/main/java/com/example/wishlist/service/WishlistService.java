package com.example.wishlist.service;


import com.example.wishlist.client.ProductoClient;
import com.example.wishlist.client.UsuarioClient;
import com.example.wishlist.dto.WishlistRequest;
import com.example.wishlist.model.Producto;
import com.example.wishlist.model.Usuario;
import com.example.wishlist.model.Wishlist;
import com.example.wishlist.repository.WishlistRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository repository;
    private final ProductoClient productoClient;
    private final UsuarioClient usuarioClient;

    public Wishlist save(WishlistRequest request) {

        Usuario usuario = usuarioClient
                .obtenerUsuario(request.getUsuarioId())
                .block();

        if (usuario == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuario no encontrado"
            );
        }

        Producto producto = productoClient
                .obtenerProducto(request.getProductoId())
                .block();

        if (producto == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Producto no encontrado"
            );
        }

        if (repository.existsByUsuarioIdAndProductoId(
                request.getUsuarioId(), request.getProductoId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "El producto ya está en la wishlist"
            );
        }

        Wishlist item = new Wishlist();
        item.setUsuarioId(usuario.getId());
        item.setUsernameUsuario(usuario.getUsername());
        item.setProductoId(producto.getId());
        item.setNombreProducto(producto.getNombre());
        item.setPrecioProducto(producto.getPrecio());

        return repository.save(item);
    }

    public List<Wishlist> getByUsuario(Long usuarioId) {
        List<Wishlist> items = repository.findByUsuarioId(usuarioId);

        if (items.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No hay items en la wishlist de el usuario ingresado"
            );
        }

        return items;
    }


    @Transactional
    public void eliminar(Long usuarioId, Long productoId) {
        if (repository.existsByUsuarioIdAndProductoId(usuarioId, productoId)) {
            repository.deleteByUsuarioIdAndProductoId(usuarioId, productoId);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "El producto no se encuentra en la wishlist del usuario ingresado"
            );
        }
    }




}

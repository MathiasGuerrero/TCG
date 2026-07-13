package com.example.wishlist.service;


import com.example.wishlist.client.ProductoClient;
import com.example.wishlist.client.UsuarioClient;
import com.example.wishlist.dto.WishlistRequest;
import com.example.wishlist.model.Producto;
import com.example.wishlist.model.Usuario;
import com.example.wishlist.model.Wishlist;
import com.example.wishlist.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WishlistServiceTest {

    @Mock
    private WishlistRepository repository;

    @Mock
    private ProductoClient productoClient;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private WishlistService wishlistService;

    @Captor
    private ArgumentCaptor<Wishlist> wishlistCaptor;

    private Usuario usuario;
    private Producto producto;
    private WishlistRequest request;

    @BeforeEach
    void setUp() {
        usuario = new Usuario(1L, "jdoe", "jdoe@correo.com");
        producto = new Producto(10L, new BigDecimal("19990"), "Carta Rara", 5);
        request = new WishlistRequest(1L, 10L);
    }

    // ---------- save() ----------

    @Test
    @DisplayName("save: guarda correctamente cuando usuario y producto existen y no está duplicado")
    void deberiaGuardarWishlistCorrectamente() {
        when(usuarioClient.obtenerUsuario(1L)).thenReturn(Mono.just(usuario));
        when(productoClient.obtenerProducto(10L)).thenReturn(Mono.just(producto));
        when(repository.existsByUsuarioIdAndProductoId(1L, 10L)).thenReturn(false);
        when(repository.save(any(Wishlist.class))).thenAnswer(inv -> inv.getArgument(0));

        Wishlist resultado = wishlistService.save(request);

        assertThat(resultado.getUsuarioId()).isEqualTo(1L);
        assertThat(resultado.getUsernameUsuario()).isEqualTo("jdoe");
        assertThat(resultado.getProductoId()).isEqualTo(10L);
        assertThat(resultado.getNombreProducto()).isEqualTo("Carta Rara");
        assertThat(resultado.getPrecioProducto()).isEqualByComparingTo("19990");

        verify(repository).save(wishlistCaptor.capture());
        assertThat(wishlistCaptor.getValue().getUsuarioId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("save: lanza 404 cuando el usuario no existe")
    void deberiaLanzar404SiUsuarioNoExiste() {
        when(usuarioClient.obtenerUsuario(1L)).thenReturn(Mono.empty());

        assertThatThrownBy(() -> wishlistService.save(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Usuario no encontrado");

        verify(productoClient, never()).obtenerProducto(anyLong());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("save: lanza 404 cuando el producto no existe")
    void deberiaLanzar404SiProductoNoExiste() {
        when(usuarioClient.obtenerUsuario(1L)).thenReturn(Mono.just(usuario));
        when(productoClient.obtenerProducto(10L)).thenReturn(Mono.empty());

        assertThatThrownBy(() -> wishlistService.save(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Producto no encontrado");

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("save: lanza 409 cuando el producto ya está en la wishlist del usuario")
    void deberiaLanzar409SiYaExisteEnWishlist() {
        when(usuarioClient.obtenerUsuario(1L)).thenReturn(Mono.just(usuario));
        when(productoClient.obtenerProducto(10L)).thenReturn(Mono.just(producto));
        when(repository.existsByUsuarioIdAndProductoId(1L, 10L)).thenReturn(true);

        assertThatThrownBy(() -> wishlistService.save(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("ya está en la wishlist");

        verify(repository, never()).save(any());
    }

    // ---------- getByUsuario() ----------

    @Test
    @DisplayName("getByUsuario: retorna la lista cuando existen items")
    void deberiaRetornarItemsDeWishlist() {
        Wishlist item = new Wishlist(1L, 1L, "jdoe", 10L, "Carta Rara", new BigDecimal("19990"));
        when(repository.findByUsuarioId(1L)).thenReturn(List.of(item));

        List<Wishlist> resultado = wishlistService.getByUsuario(1L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombreProducto()).isEqualTo("Carta Rara");
    }

    @Test
    @DisplayName("getByUsuario: lanza 404 cuando el usuario no tiene items")
    void deberiaLanzar404SiWishlistVacia() {
        when(repository.findByUsuarioId(1L)).thenReturn(List.of());

        assertThatThrownBy(() -> wishlistService.getByUsuario(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No hay items");
    }

    // ---------- eliminar() ----------

    @Test
    @DisplayName("eliminar: elimina el item cuando existe")
    void deberiaEliminarItemExistente() {
        when(repository.existsByUsuarioIdAndProductoId(1L, 10L)).thenReturn(true);

        wishlistService.eliminar(1L, 10L);

        verify(repository).deleteByUsuarioIdAndProductoId(1L, 10L);
    }

    @Test
    @DisplayName("eliminar: lanza 404 cuando el item no existe")
    void deberiaLanzar404SiItemNoExisteAlEliminar() {
        when(repository.existsByUsuarioIdAndProductoId(1L, 10L)).thenReturn(false);

        assertThatThrownBy(() -> wishlistService.eliminar(1L, 10L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("no se encuentra en la wishlist");

        verify(repository, never()).deleteByUsuarioIdAndProductoId(anyLong(), anyLong());
    }
}
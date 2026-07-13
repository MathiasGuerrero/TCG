package com.example.pedidos.service;

import com.example.pedidos.client.ProductoClient;
import com.example.pedidos.client.ReservaClient;
import com.example.pedidos.client.UsuarioClient;
import com.example.pedidos.dto.DetallePedidoRequest;
import com.example.pedidos.dto.PedidoRequest;
import com.example.pedidos.model.Pedido;
import com.example.pedidos.model.Producto;
import com.example.pedidos.model.Usuario;
import com.example.pedidos.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository repository;

    @Mock
    private ProductoClient productoClient;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private ReservaClient reservaClient;

    @InjectMocks
    private PedidoService pedidoService;

    private Usuario usuario;
    private Producto producto;
    private PedidoRequest request;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("jdoe");

        producto = new Producto();
        producto.setId(10L);
        producto.setNombre("Carta Rara");
        producto.setStock(5);

        DetallePedidoRequest detalleReq = new DetallePedidoRequest();
        detalleReq.setProductoId(10L);
        detalleReq.setCantidad(2);

        request = new PedidoRequest();
        request.setUsuarioId(1L);
        request.setDetalles(List.of(detalleReq));
    }

    // ---------- crear() ----------

    @Test
    @DisplayName("crear: guarda y retorna el pedido tal cual se recibe")
    void deberiaCrearPedidoCorrectamente() {
        Pedido pedido = new Pedido();
        when(repository.save(pedido)).thenReturn(pedido);

        Pedido resultado = pedidoService.crear(pedido);

        assertThat(resultado).isEqualTo(pedido);
        verify(repository).save(pedido);
    }

    // ---------- getPedidos() ----------

    @Test
    @DisplayName("getPedidos: retorna la lista de pedidos (sin validar vacío)")
    void deberiaRetornarListaDePedidos() {
        Pedido pedido = new Pedido();
        when(repository.findAll()).thenReturn(List.of(pedido));

        List<Pedido> resultado = pedidoService.getPedidos();

        assertThat(resultado).hasSize(1);
    }

    @Test
    @DisplayName("getPedidos: retorna lista vacía cuando no hay pedidos (sin lanzar excepción)")
    void deberiaRetornarListaVaciaSiNoHayPedidos() {
        when(repository.findAll()).thenReturn(List.of());

        List<Pedido> resultado = pedidoService.getPedidos();

        assertThat(resultado).isEmpty();
    }

    // ---------- filtrarByUsuarioID() ----------

    @Test
    @DisplayName("filtrarByUsuarioID: retorna la lista cuando el usuario tiene pedidos")
    void deberiaRetornarPedidosPorUsuario() {
        Pedido pedido = new Pedido();
        when(repository.findByUsuarioId(1L)).thenReturn(List.of(pedido));

        List<Pedido> resultado = pedidoService.filtrarByUsuarioID(1L);

        assertThat(resultado).hasSize(1);
    }

    @Test
    @DisplayName("filtrarByUsuarioID: lanza 404 cuando el usuario no tiene pedidos")
    void deberiaLanzar404SiUsuarioSinPedidos() {
        when(repository.findByUsuarioId(99L)).thenReturn(List.of());

        assertThatThrownBy(() -> pedidoService.filtrarByUsuarioID(99L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No se encontraron pedidos del usuario ingresado");
    }

    // ---------- crearPedido() ----------

    @Test
    @DisplayName("crearPedido: crea el pedido con sus detalles cuando usuario y productos son válidos")
    void deberiaCrearPedidoConDetallesCorrectamente() {
        when(usuarioClient.obtenerUsuario(1L)).thenReturn(Mono.just(usuario));
        when(productoClient.obtenerproducto(10L)).thenReturn(Mono.just(producto));
        when(repository.save(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        Pedido resultado = pedidoService.crearPedido(request);

        assertThat(resultado.getUsuarioId()).isEqualTo(1L);
        assertThat(resultado.getUsernameUsuario()).isEqualTo("jdoe");
        assertThat(resultado.getDetalles()).hasSize(1);
        assertThat(resultado.getDetalles().get(0).getProductoId()).isEqualTo(10L);
        assertThat(resultado.getDetalles().get(0).getNombreProducto()).isEqualTo("Carta Rara");
        assertThat(resultado.getDetalles().get(0).getCantidad()).isEqualTo(2);

        verify(repository).save(any(Pedido.class));
    }

    @Test
    @DisplayName("crearPedido: lanza 404 cuando el usuario no existe")
    void deberiaLanzar404SiUsuarioNoExisteAlCrearPedido() {
        when(usuarioClient.obtenerUsuario(1L)).thenReturn(Mono.empty());

        assertThatThrownBy(() -> pedidoService.crearPedido(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Usuario no encontrado");

        verify(productoClient, never()).obtenerproducto(anyLong());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("crearPedido: lanza 404 cuando un producto del detalle no existe")
    void deberiaLanzar404SiProductoNoExiste() {
        when(usuarioClient.obtenerUsuario(1L)).thenReturn(Mono.just(usuario));
        when(productoClient.obtenerproducto(10L)).thenReturn(Mono.empty());

        assertThatThrownBy(() -> pedidoService.crearPedido(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Producto no encontrado");

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("crearPedido: lanza 400 cuando el stock es insuficiente")
    void deberiaLanzar400SiStockInsuficiente() {
        producto.setStock(1); // request pide 2, stock disponible es 1

        when(usuarioClient.obtenerUsuario(1L)).thenReturn(Mono.just(usuario));
        when(productoClient.obtenerproducto(10L)).thenReturn(Mono.just(producto));

        assertThatThrownBy(() -> pedidoService.crearPedido(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Stock insuficiente para Carta Rara");

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("crearPedido: corta la validación en el primer producto inválido sin revisar los siguientes")
    void deberiaCortarEnElPrimerProductoConStockInsuficiente() {
        DetallePedidoRequest detalle1 = new DetallePedidoRequest();
        detalle1.setProductoId(10L);
        detalle1.setCantidad(10); // más que el stock disponible (5)

        DetallePedidoRequest detalle2 = new DetallePedidoRequest();
        detalle2.setProductoId(20L);
        detalle2.setCantidad(1);

        request.setDetalles(List.of(detalle1, detalle2));

        when(usuarioClient.obtenerUsuario(1L)).thenReturn(Mono.just(usuario));
        when(productoClient.obtenerproducto(10L)).thenReturn(Mono.just(producto));

        assertThatThrownBy(() -> pedidoService.crearPedido(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Stock insuficiente para Carta Rara");

        verify(productoClient, never()).obtenerproducto(20L);
        verify(repository, never()).save(any());
    }
}

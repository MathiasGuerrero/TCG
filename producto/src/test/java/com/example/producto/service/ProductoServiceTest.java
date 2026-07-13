package com.example.producto.service;

import com.example.producto.model.Producto;
import com.example.producto.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository repository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Carta Rara");
        producto.setPrecio(new BigDecimal("19990"));
        producto.setStock(5);
    }

    // ---------- getProductos() ----------

    @Test
    @DisplayName("getProductos: retorna la lista cuando hay productos registrados")
    void deberiaRetornarListaDeProductos() {
        when(repository.findAll()).thenReturn(List.of(producto));

        List<Producto> resultado = productoService.getProductos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Carta Rara");
    }

    @Test
    @DisplayName("getProductos: lanza 404 cuando no hay productos registrados")
    void deberiaLanzar404SiNoHayProductos() {
        when(repository.findAll()).thenReturn(List.of());

        assertThatThrownBy(() -> productoService.getProductos())
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No hay productos registrados");
    }

    // ---------- findById() ----------

    @Test
    @DisplayName("findById: retorna el producto cuando existe")
    void deberiaRetornarProductoPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(producto));

        Producto resultado = productoService.findById(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("findById: lanza 404 cuando no existe producto con ese ID")
    void deberiaLanzar404SiProductoNoExistePorId() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.findById(99L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No existe producto con el ID ingresado");
    }

    // ---------- findByNombre() ----------

    @Test
    @DisplayName("findByNombre: retorna el producto cuando existe")
    void deberiaRetornarProductoPorNombre() {
        when(repository.findByNombre("Carta Rara")).thenReturn(Optional.of(producto));

        Producto resultado = productoService.findByNombre("Carta Rara");

        assertThat(resultado.getNombre()).isEqualTo("Carta Rara");
    }

    @Test
    @DisplayName("findByNombre: lanza 404 cuando no existe producto con ese nombre")
    void deberiaLanzar404SiProductoNoExistePorNombre() {
        when(repository.findByNombre("Inexistente")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.findByNombre("Inexistente"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No existe producto con el nombre ingresado");
    }

    // ---------- saveProducto() ----------

    @Test
    @DisplayName("saveProducto: guarda el producto cuando el nombre no está en uso")
    void deberiaGuardarProductoCorrectamente() {
        when(repository.findByNombre("Carta Rara")).thenReturn(Optional.empty());
        when(repository.save(producto)).thenReturn(producto);

        Producto resultado = productoService.saveProducto(producto);

        assertThat(resultado.getNombre()).isEqualTo("Carta Rara");
        verify(repository).save(producto);
    }

    @Test
    @DisplayName("saveProducto: lanza 409 cuando ya existe un producto con ese nombre")
    void deberiaLanzar409SiNombreYaExiste() {
        when(repository.findByNombre("Carta Rara")).thenReturn(Optional.of(producto));

        assertThatThrownBy(() -> productoService.saveProducto(producto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Ya existe un producto con el nombre ingresado");

        verify(repository, never()).save(any());
    }

    // ---------- deleteById() ----------

    @Test
    @DisplayName("deleteById: elimina el producto cuando existe")
    void deberiaEliminarProductoExistente() {
        when(repository.findById(1L)).thenReturn(Optional.of(producto));

        productoService.deleteById(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteById: lanza 404 cuando el producto no existe")
    void deberiaLanzar404SiProductoNoExisteAlEliminar() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.deleteById(99L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No se encontró ningún producto con el ID ingresado");

        verify(repository, never()).deleteById(anyLong());
    }

    // ---------- findByPrecioBetween() ----------

    @Test
    @DisplayName("findByPrecioBetween: retorna la lista cuando hay productos en el rango de precios")
    void deberiaRetornarProductosPorRangoDePrecio() {
        BigDecimal min = new BigDecimal("10000");
        BigDecimal max = new BigDecimal("25000");
        when(repository.findByPrecioBetween(min, max)).thenReturn(List.of(producto));

        List<Producto> resultado = productoService.findByPrecioBetween(min, max);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getPrecio()).isEqualByComparingTo("19990");
    }

    @Test
    @DisplayName("findByPrecioBetween: lanza 404 cuando no hay productos en el rango de precios")
    void deberiaLanzar404SiNoHayProductosEnElRango() {
        BigDecimal min = new BigDecimal("100000");
        BigDecimal max = new BigDecimal("200000");
        when(repository.findByPrecioBetween(min, max)).thenReturn(List.of());

        assertThatThrownBy(() -> productoService.findByPrecioBetween(min, max))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("No hay productos dentro del rango de precios ingresado");
    }
}
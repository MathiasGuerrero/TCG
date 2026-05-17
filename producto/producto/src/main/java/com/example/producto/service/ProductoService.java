package com.example.producto.service;

import com.example.producto.model.Producto;
import com.example.producto.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository repository;

    public List<Producto> getProductos(){

        List<Producto> productos = repository.findAll();

        if (productos.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No hay productos registrados"
            );
        } else {

            return productos;
        }
    }

    public Producto findById(Long id){
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "No existe producto con el ID ingresado"
        ));
    }

    public Producto findByNombre(String nombre){
        return repository.findByNombre(nombre).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "No existe producto con el nombre ingresado"
        ));
    }

    public Producto saveProducto(Producto producto){
        if (repository.findByNombre(producto.getNombre()).isPresent()){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Ya existe un producto con el nombre ingresado"
            );
        } else {
            return repository.save(producto);
        }
    }

    public void deleteById(Long id){
        if (repository.findById(id).isPresent()){
            repository.deleteById(id);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No se encontró ningún producto con el ID ingresado"
            );
        }

    }

    public List<Producto> findByPrecioBetween(BigDecimal min, BigDecimal max){
        List<Producto> productos = repository.findByPrecioBetween(min, max);

        if (productos.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No hay productos dentro del rango de precios ingresado"
            );
        } else {
            return productos;
        }
    }







}

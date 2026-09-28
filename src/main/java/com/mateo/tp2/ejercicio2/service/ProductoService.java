package com.mateo.tp2.ejercicio2.service;

import com.mateo.tp2.ejercicio2.dto.ProductoDTO;
import com.mateo.tp2.exception.ResourceNotFoundException;
import com.mateo.tp2.ejercicio2.model.Producto;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;



@Service
public class ProductoService {

    private final List<Producto> productos = new ArrayList<>();
    private final AtomicLong contadorId = new AtomicLong(1);

    @PostConstruct
    public void cargarDatosIniciales() {
        agregarProducto(new ProductoDTO("Mouse inalambrico", "Perifericos", 4500, 15));
        agregarProducto(new ProductoDTO("Teclado mecanico", "Perifericos", 25000, 8));
        agregarProducto(new ProductoDTO("Monitor 24 pulgadas", "Monitores", 120000, 5));
        agregarProducto(new ProductoDTO("Notebook i5", "Notebooks", 850000, 3));
        agregarProducto(new ProductoDTO("Auriculares gamer", "Audio", 18000, 20));
        agregarProducto(new ProductoDTO("Webcam HD", "Perifericos", 15000, 10));
        agregarProducto(new ProductoDTO("Disco SSD 480GB", "Almacenamiento", 32000, 12));
        agregarProducto(new ProductoDTO("Memoria RAM 8GB", "Componentes", 21000, 18));
    }

    public List<Producto> listarTodos() {
        return productos;
    }

    public Producto agregarProducto(ProductoDTO dto) {
        Producto nuevo = new Producto(
                contadorId.getAndIncrement(),
                dto.getNombre(),
                dto.getCategoria(),
                dto.getPrecio(),
                dto.getStock()
        );
        productos.add(nuevo);
        return nuevo;
    }


    public List<Producto> buscar(String categoria, Double precioMin, Double precioMax) {
    return productos.stream()
            .filter(p -> categoria == null || p.getCategoria().equalsIgnoreCase(categoria))
            .filter(p -> precioMin == null || p.getPrecio() >= precioMin)
            .filter(p -> precioMax == null || p.getPrecio() <= precioMax)
            .collect(Collectors.toList());
    }

    public List<Producto> ordenar(String criterio, String orden) {
        Comparator<Producto> comparador;

        if ("nombre".equalsIgnoreCase(criterio)) {
            comparador = Comparator.comparing(Producto::getNombre, String.CASE_INSENSITIVE_ORDER);
        } else {
            // por defecto o si viene "precio"
            comparador = Comparator.comparingDouble(Producto::getPrecio);
        }

        if ("desc".equalsIgnoreCase(orden)) {
            comparador = comparador.reversed();
        }

        return productos.stream()
                .sorted(comparador)
                .collect(Collectors.toList());
    }

    public Producto modificarStock(Long id, int cantidad) {
    Producto producto = productos.stream()
            .filter(p -> p.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("No se encontró un producto con id " + id));

    int nuevoStock = producto.getStock() + cantidad;

    if (nuevoStock < 0) {
        throw new IllegalArgumentException("El stock no puede quedar en un valor negativo");
    }

    producto.setStock(nuevoStock);
    return producto;
    }

    public void eliminarProducto(Long id) {
        boolean eliminado = productos.removeIf(p -> p.getId().equals(id));

        if (!eliminado) {
            throw new ResourceNotFoundException("No se encontró un producto con id " + id);
        }
    }




}
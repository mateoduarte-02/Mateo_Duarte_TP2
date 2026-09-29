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



@Service                            // Spring crea una instancia y la deja disponible para inyectar
public class ProductoService {

    private final List<Producto> productos = new ArrayList<>();               // nuestra "base de datos" en memoria
    private final AtomicLong contadorId = new AtomicLong(1);    // contador de ids que arranca en 1

    // final significa que la variable no se puede reasignar, pero sí se pueden agregar y quitar elementos de la lista. 
    // AtomicLong es un contador seguro para uso simultáneo.

    @PostConstruct          // Spring ejecuta este método UNA vez, apenas crea el Service
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
        return productos;                   // devuelve la lista completa
    }

    public Producto agregarProducto(ProductoDTO dto) {
        Producto nuevo = new Producto(
                contadorId.getAndIncrement(),       // devuelve el valor actual y después suma 1 (1, 2, 3...)
                dto.getNombre(),
                dto.getCategoria(),
                dto.getPrecio(),
                dto.getStock()
        );
        productos.add(nuevo);       // lo agrega a la lista
        return nuevo;               // lo devuelve para mostrarlo con el id ya asignado
    }


    public List<Producto> buscar(String categoria, Double precioMin, Double precioMax) {
    return productos.stream()                           // convierte la lista en un flujo de datos
            .filter(p -> categoria == null || p.getCategoria().equalsIgnoreCase(categoria))     // equalsIgnoreCase hace que "perifericos" y "Perifericos" den lo mismo.
            .filter(p -> precioMin == null || p.getPrecio() >= precioMin)
            .filter(p -> precioMax == null || p.getPrecio() <= precioMax)
            .collect(Collectors.toList());              // convierte el flujo otra vez en lista
    }

    // p -> ... es una lambda. p es cada producto, y filter se queda con los que devuelven true.
    // categoria == null || ... es el truco de los filtros opcionales. Si no mandaron el parámetro, la condición es true y ese filtro deja pasar todo.
    // Se combinan con AND porque cada filter trabaja sobre lo que dejó el anterior.
    // Si no mandan ningún parámetro, no filtra nada y devuelve los 8.


    public List<Producto> ordenar(String criterio, String orden) {
        Comparator<Producto> comparador;

        if ("nombre".equalsIgnoreCase(criterio)) {      //No hago criterio.equalsIgnoreCase("nombre") para evitar NullPointerException si criterio es null.
            comparador = Comparator.comparing(Producto::getNombre, String.CASE_INSENSITIVE_ORDER);  //CASE_INSENSITIVE_ORDER ordena sin distinguir mayúsculas de minúsculas.
        } else {
            // por defecto o si viene "precio"
            comparador = Comparator.comparingDouble(Producto::getPrecio);
        }

        if ("desc".equalsIgnoreCase(orden)) {
            comparador = comparador.reversed();     // invierte el criterio
        }

        return productos.stream()       // convierte la lista en un flujo de productos
                .sorted(comparador)     // los ordena según el comparador
                .collect(Collectors.toList());      // arma una lista nueva con el resultado
    }

    // Comparator define cómo se comparan dos productos. 
    // Producto::getNombre es una referencia a método (equivale a p -> p.getNombre()).





    public Producto modificarStock(Long id, int cantidad) {
    Producto producto = productos.stream()
            .filter(p -> p.getId().equals(id))          // equals y no ==, porque son objetos Long
            .findFirst()    // toma el primero, devuelve un Optional que es como una caja que puede tener un producto o estar vacía (evita devolver null y el NullPointerException)
            .orElseThrow(() -> new ResourceNotFoundException("No se encontró un producto con id " + id));   //abre la caja, si hay producto lo devuelve y si está vacía lanza ResourceNotFoundException

    int nuevoStock = producto.getStock() + cantidad;

    if (nuevoStock < 0) {
        throw new IllegalArgumentException("El stock no puede quedar en un valor negativo");
    }

    producto.setStock(nuevoStock);       //guardo el nuevo stock en el producto
    return producto;
    }




    public void eliminarProducto(Long id) {
        boolean eliminado = productos.removeIf(p -> p.getId().equals(id));          // removeIf recorre la lista y borra los productos que cumplen la condición.
                                                                                    // Devuelve true si borró al menos uno, y false si no encontró ninguno.
        if (!eliminado) {
            throw new ResourceNotFoundException("No se encontró un producto con id " + id);
        }                               // Si no borró nada, es porque el id no existe: lanzamos el 404.
    }




}
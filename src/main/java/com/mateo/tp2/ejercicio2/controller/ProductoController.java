package com.mateo.tp2.ejercicio2.controller;

import com.mateo.tp2.dto.ApiResponse;
import com.mateo.tp2.ejercicio2.dto.ProductoDTO;
import com.mateo.tp2.ejercicio2.model.Producto;
import com.mateo.tp2.ejercicio2.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController                    // esta clase atiende peticiones HTTP y responde en JSON
@RequestMapping("/api/catalogo")        // define el prefijo común de todas las rutas del controller (todos los endpoints de esta clase empiezan con /api/catalogo)
@Tag(name = "Catálogo", description = "Gestión y búsqueda de productos en memoria")
public class ProductoController {

    private final ProductoService productoService;

    // Inyección de dependencias: Spring crea el Service y lo pasa acá.
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los productos del catálogo")
    public ResponseEntity<ApiResponse<List<Producto>>> listarTodos() {
        List<Producto> productos = productoService.listarTodos();       // El controller no piensa, le pide la lista al Service.
        
        ApiResponse<List<Producto>> response = new ApiResponse<>(       // Se empaqueta en el formato estándar (status, messege y data)
                HttpStatus.OK.value(), "Productos obtenidos con éxito", productos
        );
        return ResponseEntity.ok(response);     // respuesta HTTP 200
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar productos con filtros opcionales combinables",
            description = "Filtra por categoría, precio mínimo y/o precio máximo. Todos los parámetros son opcionales.")
    public ResponseEntity<ApiResponse<List<Producto>>> buscar(
                // Los 3 filtros vienen de la URL (?categoria=...) y son opcionales
                // si no se envían llegan como null y el Service no los aplica
            @Parameter(description = "Categoría a filtrar") @RequestParam(required = false) String categoria,
            @Parameter(description = "Precio mínimo") @RequestParam(required = false) Double precioMin,
            @Parameter(description = "Precio máximo") @RequestParam(required = false) Double precioMax) {

        List<Producto> productos = productoService.buscar(categoria, precioMin, precioMax);
        ApiResponse<List<Producto>> response = new ApiResponse<>(
                HttpStatus.OK.value(), "Búsqueda realizada con éxito", productos
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ordenar")
    @Operation(summary = "Ordenar productos por precio o nombre",
            description = "criterio: 'precio' o 'nombre'. orden: 'asc' (por defecto) o 'desc'.")
    public ResponseEntity<ApiResponse<List<Producto>>> ordenar(
        
            // el criterio es obligatorio, si falta Spring responde con error.
            @Parameter(description = "Criterio de ordenamiento: precio o nombre") @RequestParam String criterio,
            // el orden es opcional, si no lo mandan defaultValue lo pone en "asc".
            @Parameter(description = "Orden: asc o desc") @RequestParam(required = false, defaultValue = "asc") String orden) {

        List<Producto> productos = productoService.ordenar(criterio, orden);
        ApiResponse<List<Producto>> response = new ApiResponse<>(
                HttpStatus.OK.value(), "Productos ordenados con éxito", productos
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Agregar un nuevo producto al catálogo")
    public ResponseEntity<ApiResponse<Producto>> agregarProducto(@Valid @RequestBody ProductoDTO productoDTO) {
        Producto nuevoProducto = productoService.agregarProducto(productoDTO);
        ApiResponse<Producto> response = new ApiResponse<>(
                HttpStatus.CREATED.value(), "Producto creado con éxito", nuevoProducto
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/stock")
    @Operation(summary = "Modificar el stock de un producto",
            description = "cantidad positiva aumenta el stock, negativa lo disminuye. No puede quedar en negativo.")
    public ResponseEntity<ApiResponse<Producto>> modificarStock(
            @Parameter(description = "Id del producto") @PathVariable Long id,
            @Parameter(description = "Cantidad a sumar (o restar si es negativa)") @RequestParam int cantidad) {

        Producto producto = productoService.modificarStock(id, cantidad);
        ApiResponse<Producto> response = new ApiResponse<>(
                HttpStatus.OK.value(), "Stock modificado con éxito", producto
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto del catálogo")
    public ResponseEntity<ApiResponse<Void>> eliminarProducto(
            @Parameter(description = "Id del producto a eliminar") @PathVariable Long id) {

        productoService.eliminarProducto(id);
        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.OK.value(), "Producto eliminado con éxito", null
        );
        return ResponseEntity.ok(response);
    }
}
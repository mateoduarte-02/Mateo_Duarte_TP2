package com.mateo.tp2.ejercicio5.service;

import com.mateo.tp2.ejercicio5.dto.PedidoRespuestaDTO;
import com.mateo.tp2.ejercicio5.dto.ProductoPedidoDTO;
import com.mateo.tp2.ejercicio5.entity.DetallePedido;
import com.mateo.tp2.ejercicio5.entity.Pedido;
import com.mateo.tp2.ejercicio5.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<PedidoRespuestaDTO> buscarConFiltros(
            Long clienteId, String categoria, LocalDate fechaDesde, LocalDate fechaHasta, String estado) {

        List<Pedido> pedidos = pedidoRepository.buscarConFiltros(
                clienteId, categoria, fechaDesde, fechaHasta, estado);

        return pedidos.stream()
                .map(this::mapearAPedidoRespuestaDTO)
                .collect(Collectors.toList());
    }

    private PedidoRespuestaDTO mapearAPedidoRespuestaDTO(Pedido pedido) {

        List<ProductoPedidoDTO> productos = pedido.getDetalles().stream()
                .map(this::mapearADetalleDTO)
                .collect(Collectors.toList());

        double totalPedido = productos.stream()
                .mapToDouble(ProductoPedidoDTO::getSubtotal)
                .sum();

        String nombreCliente = pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellido();

        return new PedidoRespuestaDTO(
                pedido.getId(),
                nombreCliente,
                pedido.getFechaPedido(),
                pedido.getEstado(),
                totalPedido,
                productos
        );
    }

    private ProductoPedidoDTO mapearADetalleDTO(DetallePedido detalle) {
        double subtotal = detalle.getCantidad() * detalle.getPrecioUnitario();
        String nombreCategoria = detalle.getProducto().getCategoria() != null
                ? detalle.getProducto().getCategoria().getNombre()
                : null;

        return new ProductoPedidoDTO(
                detalle.getProducto().getNombre(),
                nombreCategoria,
                detalle.getCantidad(),
                subtotal
        );
    }
}
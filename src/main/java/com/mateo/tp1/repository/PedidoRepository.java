package com.mateo.tp1.repository;

import com.mateo.tp1.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT DISTINCT p FROM Pedido p " +
           "JOIN FETCH p.cliente c " +
           "LEFT JOIN FETCH p.detalles d " +
           "LEFT JOIN FETCH d.producto pr " +
           "LEFT JOIN FETCH pr.categoria cat " +
           "WHERE (:clienteId IS NULL OR c.id = :clienteId) " +
           "AND (:estado IS NULL OR p.estado = :estado) " +
           "AND (:fechaDesde IS NULL OR p.fechaPedido >= :fechaDesde) " +
           "AND (:fechaHasta IS NULL OR p.fechaPedido <= :fechaHasta) " +
           "AND (:categoria IS NULL OR EXISTS (" +
           "    SELECT 1 FROM DetallePedido d2 " +
           "    WHERE d2.pedido = p AND d2.producto.categoria.nombre = :categoria" +
           "))")
    List<Pedido> buscarConFiltros(
            @Param("clienteId") Long clienteId,
            @Param("categoria") String categoria,
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            @Param("estado") String estado
    );
}
package com.mateo.tp2.ejercicio5.entity;

import com.mateo.tp2.ejercicio4.entity.Cliente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(name = "fecha_pedido")
    private LocalDate fechaPedido;

    private String estado;

    @OneToMany(mappedBy = "pedido")
    private List<DetallePedido> detalles;
}
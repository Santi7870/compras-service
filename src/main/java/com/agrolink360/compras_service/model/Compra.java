package com.agrolink360.compras_service.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data // Lombok genera los getters y setters automáticamente
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID de la compra

    private Long productoId; // ID del producto comprado

    private String producto;
    private int cantidad;
    private double precioUnitario;
    private String comprador;
    private LocalDate fecha;
    private boolean enviado = true;
    private Long transportistaId; // ID del transportista asignado
    private String estadoEntrega;

    private Double distanciaKm;
    private Integer duracionSegundos;
    private Double costoEstimado;
    private String direccionDestino; // ejemplo decorativo
// "pendiente", "en tránsito", "entregado"
// opcional: puedes usarlo luego para control

}


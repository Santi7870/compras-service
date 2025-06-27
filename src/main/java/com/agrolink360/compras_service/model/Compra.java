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
}


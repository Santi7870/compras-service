package com.agrolink360.compras_service.dto;

import lombok.Data;

@Data
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private double precioUnitario;
    private int stock;
}

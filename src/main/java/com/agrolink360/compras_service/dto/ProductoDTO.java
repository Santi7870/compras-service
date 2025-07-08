package com.agrolink360.compras_service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nombre;
    private String descripcion;
    private double precioUnitario;
    private int stock;
}


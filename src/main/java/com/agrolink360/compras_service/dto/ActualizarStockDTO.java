package com.agrolink360.compras_service.dto;

import lombok.Data;

@Data
public class ActualizarStockDTO {
    private Long productoId;
    private int cantidadRestar;
}



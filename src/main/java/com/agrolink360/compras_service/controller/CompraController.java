package com.agrolink360.compras_service.controller;

import com.agrolink360.compras_service.dto.ActualizarStockDTO;
import com.agrolink360.compras_service.dto.EnvioRequest;
import com.agrolink360.compras_service.dto.ProductoDTO;
import com.agrolink360.compras_service.model.Compra;
import com.agrolink360.compras_service.repository.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "*")
public class CompraController {

    private final CompraRepository compraRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public CompraController(CompraRepository compraRepository, RestTemplate restTemplate) {
        this.compraRepository = compraRepository;
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public ResponseEntity<Compra> crearCompra(@RequestBody Compra compra) {
        // 1. Guardar la compra
        Compra nuevaCompra = compraRepository.save(compra);

        // ✅ 2. Restar stock al producto comprado
        ActualizarStockDTO stockDTO = new ActualizarStockDTO();
        stockDTO.setProductoId(compra.getProductoId()); // <-- AQUÍ LO COLOCAS
        stockDTO.setCantidadRestar(compra.getCantidad());

        restTemplate.put("http://localhost:8085/api/productos/restar-stock", stockDTO);

        // 3. Crear objeto EnvioRequest
        EnvioRequest envio = new EnvioRequest();
        envio.setProducto(compra.getProducto());
        envio.setCantidad(compra.getCantidad());
        envio.setDireccionDestino("Quito");

        // 4. Crear el envío
        restTemplate.postForObject("http://localhost:8082/api/envios", envio, Void.class);

        return new ResponseEntity<>(nuevaCompra, HttpStatus.CREATED);
    }


    @GetMapping
    public List<Compra> obtenerTodas() {
        return compraRepository.findAll();
    }

    @GetMapping("/productos-disponibles")
    public ResponseEntity<ProductoDTO[]> obtenerProductosDisponibles() {
        String url = "http://localhost:8081/api/productos";
        ProductoDTO[] productos = restTemplate.getForObject(url, ProductoDTO[].class);
        return ResponseEntity.ok(productos);
    }


}

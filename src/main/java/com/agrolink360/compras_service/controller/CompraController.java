package com.agrolink360.compras_service.controller;

import com.agrolink360.compras_service.dto.ActualizarStockDTO;
import com.agrolink360.compras_service.dto.CompraAsignadaDTO;
import com.agrolink360.compras_service.dto.ProductoDTO;
import com.agrolink360.compras_service.model.Compra;
import com.agrolink360.compras_service.repository.CompraRepository;
import com.agrolink360.compras_service.service.ProductoCacheService;
import com.agrolink360.compras_service.service.RoutingService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private final CompraRepository compraRepository;
    private final RestTemplate restTemplate;
    private final ProductoCacheService productoCacheService;

    @Autowired
    public CompraController(CompraRepository compraRepository,
                            RestTemplate restTemplate,
                            ProductoCacheService productoCacheService) {
        this.compraRepository = compraRepository;
        this.restTemplate = restTemplate;
        this.productoCacheService = productoCacheService;
    }

    @Autowired
    private RoutingService routingService;

    @PostMapping
    public Compra crearCompra(@RequestBody Compra compra) {
        double origenLat = -0.180653;
        double origenLon = -78.467834;

        try {
            double[] destinoCoords = routingService.obtenerCoordenadas(compra.getDireccionDestino());
            double destinoLon = destinoCoords[0];
            double destinoLat = destinoCoords[1];

            JSONObject ruta = routingService.calcularRuta(origenLon, origenLat, destinoLon, destinoLat);
            JSONObject summary = ruta.getJSONArray("features")
                    .getJSONObject(0)
                    .getJSONObject("properties")
                    .getJSONObject("summary");

            double distanciaKm = summary.getDouble("distance") / 1000.0;
            int duracionSegundos = summary.getInt("duration");
            double costoEstimado = distanciaKm * 0.25;

            compra.setDistanciaKm(distanciaKm);
            compra.setDuracionSegundos(duracionSegundos);
            compra.setCostoEstimado(costoEstimado);

            if (compra.getFecha() == null) {
                compra.setFecha(LocalDate.now());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al calcular la ruta. Verifica la ciudad ingresada.");
        }

        return compraRepository.save(compra);
    }

    @GetMapping("/invalidar-cache")
    public ResponseEntity<String> invalidarCache() {
        productoCacheService.invalidarCacheProductos();
        return ResponseEntity.ok("🧹 Caché de productos invalidada correctamente.");
    }


    @GetMapping
    public List<Compra> obtenerTodas() {
        return compraRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Compra> obtenerCompraPorId(@PathVariable Long id) {
        Optional<Compra> optionalCompra = compraRepository.findById(id);
        return optionalCompra.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/productos-disponibles")
    public ResponseEntity<ProductoDTO[]> obtenerProductosDisponibles() {
        ProductoDTO[] productos = productoCacheService.obtenerProductos();
        return ResponseEntity.ok(productos);
    }

    @PostMapping("/asignar-transportista")
    public ResponseEntity<String> asignarTransportista(@RequestBody CompraAsignadaDTO dto) {
        Compra compra = compraRepository.findById(dto.getCompraId()).orElse(null);
        if (compra == null) return ResponseEntity.notFound().build();

        compra.setTransportistaId(dto.getTransportistaId());
        compra.setEstadoEntrega("en tránsito");
        compraRepository.save(compra);

        productoCacheService.invalidarCacheProductos();
        return ResponseEntity.ok("Compra asignada al transportista.");
    }

    @PutMapping("/entregar/{id}")
    public ResponseEntity<String> entregarCompra(@PathVariable Long id) {
        Optional<Compra> optional = compraRepository.findById(id);
        if (optional.isPresent()) {
            Compra compra = optional.get();
            compra.setEstadoEntrega("entregado");
            compraRepository.save(compra);
            return ResponseEntity.ok("Compra entregada.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}


package com.agrolink360.compras_service.service;

import com.agrolink360.compras_service.dto.ProductoDTO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductoCacheService {

    private final RestTemplate restTemplate;

    public ProductoCacheService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "productos")
    public ProductoDTO[] obtenerProductos() {
        String url = "http://localhost:8085/api/productos";
        return restTemplate.getForObject(url, ProductoDTO[].class);
    }

    @CacheEvict(value = "productos", allEntries = true)
    public void invalidarCacheProductos() {
        System.out.println("🧹 Caché de productos invalidada.");
    }
}


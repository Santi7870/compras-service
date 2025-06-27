package com.agrolink360.compras_service.service;

import com.agrolink360.compras_service.model.Compra;
import com.agrolink360.compras_service.repository.CompraRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompraService {

    private final CompraRepository repository;

    public CompraService(CompraRepository repository) {
        this.repository = repository;
    }

    public List<Compra> obtenerTodas() {
        return repository.findAll();
    }

    public Compra crearCompra(Compra compra) {
        return repository.save(compra);
    }
}
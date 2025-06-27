package com.agrolink360.compras_service.repository;

import com.agrolink360.compras_service.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompraRepository extends JpaRepository<Compra, Long> {
}
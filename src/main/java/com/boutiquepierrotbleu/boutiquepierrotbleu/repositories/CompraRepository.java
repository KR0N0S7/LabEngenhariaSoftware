package com.boutiquepierrotbleu.boutiquepierrotbleu.repositories;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Compra;

public interface CompraRepository extends JpaRepository<Compra, Long> {

    List<Compra> getComprasByClienteId(Long clienteId);

    @Query(value = "SELECT MONTH(STR_TO_DATE(c.data, '%d/%m/%Y')) as mes, COUNT(*) as total FROM compra c GROUP BY MONTH(STR_TO_DATE(c.data, '%d/%m/%Y'))", nativeQuery = true)
    List<Map<String, Object>> countComprasPorMes();


}

package com.boutiquepierrotbleu.boutiquepierrotbleu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Compra;

public interface CompraRepository extends JpaRepository<Compra, Long> {

    List<Compra> getComprasByClienteId(Long clienteId);

}

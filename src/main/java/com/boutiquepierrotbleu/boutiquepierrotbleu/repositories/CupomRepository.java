package com.boutiquepierrotbleu.boutiquepierrotbleu.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Cupom;

public interface CupomRepository extends JpaRepository<Cupom, Long> {

    Optional<Cupom> findByCodigo(String cupomCodigo);

    List<Cupom> findAllByClienteId(Long clienteId);
}

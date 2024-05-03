package com.boutiquepierrotbleu.boutiquepierrotbleu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.NotasProdutos;

public interface NotasProdutosRepository extends JpaRepository<NotasProdutos, Long> {

    List<NotasProdutos> findByClienteId(Long clienteId);

}

package com.boutiquepierrotbleu.boutiquepierrotbleu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    List<Endereco> getEnderecosByClienteId(Long clienteId);
}

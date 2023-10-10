package com.boutiquepierrotbleu.boutiquepierrotbleu.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>, ClienteRepositoryCustom {
    List<Cliente> findByAtivoTrue();

    Optional<Cliente> findByEmailAndSenha(String email, String senha);
}

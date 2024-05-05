package com.boutiquepierrotbleu.boutiquepierrotbleu.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>, ClienteRepositoryCustom {
    List<Cliente> findByAtivoTrue();

    Optional<Cliente> findByEmailAndSenha(String email, String senha);

    @Query("select c from Cliente c where function('MONTH', c.dataAniversario) = ?1 and function('DAY', c.dataAniversario) = ?2")
    List<Cliente> findByDataAniversario(int mes, int dia);
}

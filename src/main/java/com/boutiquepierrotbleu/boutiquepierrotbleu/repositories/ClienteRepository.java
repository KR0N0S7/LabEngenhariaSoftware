package com.boutiquepierrotbleu.boutiquepierrotbleu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}

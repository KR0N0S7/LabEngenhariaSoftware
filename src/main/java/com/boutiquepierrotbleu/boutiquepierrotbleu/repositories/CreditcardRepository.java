package com.boutiquepierrotbleu.boutiquepierrotbleu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Creditcard;

public interface CreditcardRepository extends JpaRepository<Creditcard, Long> {

    List<Creditcard> findByClienteId(Long clienteId);

}

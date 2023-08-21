package com.boutiquepierrotbleu.boutiquepierrotbleu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}

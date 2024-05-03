package com.boutiquepierrotbleu.boutiquepierrotbleu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // List<Produto> listarProdutosAPartirDeUmaCategoria();

    // New method to find products with stock quantity more than to zero and
    // aviseMe
    // attribute set to true
    List<Produto> findByEstoqueGreaterThanAndAviseMeTrue(int stockQuantity);

    List<Produto> findByNomeIn(List<String> nomeProduto);
}

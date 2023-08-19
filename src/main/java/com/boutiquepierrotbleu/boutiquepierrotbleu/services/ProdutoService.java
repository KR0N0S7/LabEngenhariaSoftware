package com.boutiquepierrotbleu.boutiquepierrotbleu.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Produto;
import com.boutiquepierrotbleu.boutiquepierrotbleu.repositories.ProdutoRepository;

@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository produtoRepository;

    public Produto salvarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Produto obterProduto(Long id) throws Exception {
        Optional<Produto> produto = produtoRepository.findById(id);
        if (produto.isEmpty()) {
            throw new Exception("Produto não encontrado!");
        }
        return produto.get();
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    public void deletarProduto(Long id) {
        produtoRepository.deleteById(id);
    }
}

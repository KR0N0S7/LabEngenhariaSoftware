package com.boutiquepierrotbleu.boutiquepierrotbleu.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boutiquepierrotbleu.boutiquepierrotbleu.entities.Produto;
import com.boutiquepierrotbleu.boutiquepierrotbleu.integrations.JsonUtil;
import com.boutiquepierrotbleu.boutiquepierrotbleu.repositories.ProdutoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @Transactional
    public void reservarEstoque(Long id, int quantidade) throws Exception {
        Produto produto = obterProduto(id);
        produto.reserveStock(quantidade);
        produtoRepository.save(produto);
    }

    @Transactional
    public void liberarEstoque(Long id, int quantidade) throws Exception {
        Produto produto = obterProduto(id);
        produto.increaseEstoque(quantidade);
        produtoRepository.save(produto);
    }

    public void toggleAtivoStatusById(Long produtoId) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Produto Id:" + produtoId));
        produto.setAtivo(!produto.isAtivo()); // Toggle the ativo status
        produtoRepository.save(produto);
    }

    public Mono<List<Produto>> getRecommendedProducts(Mono<String> recomendacoes) {
        return recomendacoes
                .flatMap(JsonUtil::parseJsonToProductNames) // Convert JSON to List<String> of product names
                .flatMapMany(this::listarProdutosAPartirDeUmaListaDeNomesFlux) // Convert List<String> to Flux<Produto>
                .collectList(); // Collect Flux<Produto> to List<Produto>
    }

    public Flux<Produto> listarProdutosAPartirDeUmaListaDeNomesFlux(List<String> nomeProduto) {
        return Flux.fromIterable(produtoRepository.findByNomeIn(nomeProduto));
    }

    public List<Produto> listarProdutosAPartirDeUmaListaDeNomes(List<String> nomeProduto) {
        return produtoRepository.findByNomeIn(nomeProduto);
    }

    // public List<Produto> listarProdutosAPartirDeUmaCategoria() {
    // // TODO Auto-generated method stub
    // return produtoRepository.listarProdutosAPartirDeUmaCategoria();
    // }
}
